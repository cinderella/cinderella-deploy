(ns cinderella.groups.cinderella
  "Node definitions for cinderella

Creates a node-spec that will install and configure cinderella and vblob
on a single node."
  (:use
   [pallet.core :only [group-spec server-spec node-spec]]
   [pallet.crate.automated-admin-user :only [automated-admin-user]]
   [pallet.crate.cinderella
    :only [cinderella-settings configure-cinderella install-cinderella
           compute-service-details]]
   [pallet.crate.node-js :only [nodejs]]
   [pallet.crate.forever :only [forever]]
   [pallet.crate.java :only [java]]
   [pallet.crate.jetty :only [deploy init-service jetty]]
   [pallet.crate.vblob :only [vblob vblob-forever]]
   [pallet.phase :only [phase-fn]]))

(defn settings-from-compute-service
  [compute-service]
  (let [auth (compute-service-details compute-service)]
    {:backend-identity (:identity auth)
     :backend-credential (:credential auth)
     :backend-endpoint (:endpoint auth)}))

(def node-deb
  "https://raw.github.com/cinderella/deploy/master/debs/nodejs-0.6.10_amd64.deb")

(def default-node-spec
  (node-spec
   :image {:os-family :ubuntu}
   :hardware {:min-cores 1}))

(def
  ^{:doc "Defines the type of node cinderella will run on"}
  base-server
  (server-spec
   :phases
   {:bootstrap (phase-fn (automated-admin-user))}))

(defn cinderella-using-compute
  "Use the compute service from the session to extract the authorisation
information for the cinderella backend."
  [session]
  (let [{:keys [user install-path]}
        (get-target-settings session :jetty nil)]
    (cinderella-settings
     session (assoc (settings-from-compute-service (:compute session))
               :home install-path))))

(def
  ^{:doc "Define a server spec for cinderella (EC2)"}
  cinderella-server
  (server-spec
   :extends [(java {})(jetty {})]
   :phases {:settings (phase-fn
                        (cinderella-using-compute))
            :configure (phase-fn
                         (install-cinderella)
                         (configure-cinderella)
                         (init-service
                          ;; :if-config-changed true
                          :action :restart))}))
(def
  ^{:doc "Define a server spec for vblob (S3)"}
  vblob-server
  (server-spec
   :extends [(nodejs
              {:deb
               {:url node-deb
                :md5 "597250b48364b4ed7ab929fb6a8410b8"}})
             (forever {:version "0.9.2"})
             (vblob {})]
   :phases {:configure (phase-fn (vblob-forever :action :start :max 1))}))


(def
  ^{:doc "Define a server spec that exposes S3 and EC2 apis"}
  aws-adapter
  (server-spec
   :extends [vblob-server cinderella-server]))

(def
  ^{:doc "Defines a group spec that can be passed to converge or lift."}
  cinderella
  (group-spec
   "cinderella"
   :extends [base-server aws-adapter]
   :node-spec default-node-spec))
