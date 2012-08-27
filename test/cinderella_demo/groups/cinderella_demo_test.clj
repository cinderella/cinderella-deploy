(ns cinderella-demo.groups.cinderella-demo-test
  (:use
   [cinderella-demo.groups.cinderella-demo :only [cinderella-demo]]
   [org.jclouds.blobstore2 :only [blobstore containers]]
   [org.jclouds.compute2 :only [compute-service]]
   [pallet.action :only [def-clj-action]]
   [pallet.core :only [lift]]
   [pallet.crate.automated-admin-user :only [automated-admin-user]]
   [pallet.node :only [primary-ip]]
   [pallet.parameter :only [get-target-settings]]
   [pallet.phase :only [phase-fn]]
   [pallet.session :only [nodes-in-group]]
   clojure.test
   pallet.test-utils)
  (:require
   [clojure.tools.logging :as logging]
   [pallet.live-test :as live-test]))


(def-clj-action verify-vblob
  [session group-name & {:keys [instance-id]}]
  (let [{:keys [home user port keyID secretID] :as settings}
        (get-target-settings session :vblob instance-id)
        node (first (nodes-in-group session group-name))
        endpoint (format "http://%s:%s/" (primary-ip node) port)
        _ (logging/debugf "Testing with %s %s %s" keyID secretID endpoint)
        bs (blobstore "s3" keyID secretID
                      :s3.endpoint endpoint
                      :jclouds.s3.virtual-host-buckets false)]
    (is bs "Blobstore contactable")
    (is (containers bs) "Blobstore containers listable")
    (logging/infof "Blobstore containers %s" (vec (containers bs)))
    session))

(def-clj-action verify-cinderella
  [session group-name & {:keys [instance-id]}]
  (let [{:keys [home user ec2-port identity credential] :as settings}
        (get-target-settings session :cinderella instance-id)
        node (first (nodes-in-group session group-name))
        endpoint (format "http://%s:%s/" (primary-ip node) ec2-port)
        _ (logging/debugf "Testing with %s %s %s" identity credential endpoint)
        c (compute-service
           "aws-ec2" identity credential :jclouds.endpoint endpoint)
        images (.. c getContext
                   (unwrap org.jclouds.ec2.EC2ApiMetadata/CONTEXT_TOKEN)
                   getApi getAMIServices (describeImagesInRegion nil nil))]
    (is c "Compute returned")
    (is (seq images) "Compute useable")
    (logging/infof "Compute images %s" (vec images))
    session))

(deftest live-test
  (live-test/test-for
   [image [{:os-family :ubuntu :os-64-bit true}]]
   (live-test/test-nodes
    [compute node-map node-types]
    {:cinderella-demo
     (->
      cinderella-demo
      (assoc :image image :count 1)
      (update-in [:phases] assoc
                 :bootstrap (phase-fn
                              (automated-admin-user))
                 :verify (phase-fn
                           (verify-vblob :cinderella-demo)
                           (verify-cinderella :cinderella-demo))))}
    (lift (:cinderella-demo node-types) :phase :verify :compute compute))))
