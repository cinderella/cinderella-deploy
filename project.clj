(defproject cinderella-demo "0.1.0-SNAPSHOT"
  :description "Demo of CloudFoundry ec2 and s3 api's."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.cloudhoist/pallet "0.7.2"]
                 [org.cloudhoist/java "0.7.1-SNAPSHOT"]
                 [org.cloudhoist/jetty "0.7.0-SNAPSHOT"]
                 [org.cloudhoist/node-js "0.7.0-SNAPSHOT"]
                 [org.cloudhoist/forever "0.1.0-SNAPSHOT"]
                 [org.cloudhoist/vblob "0.1.0-SNAPSHOT"]
                 [org.cloudhoist/cinderella "0.1.0-SNAPSHOT"]
                 [org.cloudhoist/pallet-jclouds "1.5.0-SNAPSHOT"]
                 ;; To get started we include all jclouds compute providers.
                 ;; You may wish to replace this with the specific jclouds
                 ;; providers you use, to reduce dependency sizes.
                 [org.jclouds.provider/bluelock-vcloud-zone01 "1.5.0-beta.11"]
                 [org.jclouds.provider/aws-ec2 "1.5.0-beta.11"]
                 [org.jclouds.provider/aws-s3 "1.5.0-beta.11"]
                 [org.jclouds.driver/jclouds-slf4j "1.5.0-beta.11"]
                 [org.jclouds.driver/jclouds-sshj "1.5.0-beta.11"]
                 [org.slf4j/slf4j-api "1.6.1"]
                 [ch.qos.logback/logback-core "1.0.0"]
                 [ch.qos.logback/logback-classic "1.0.0"]]
  :dev-dependencies [[org.cloudhoist/pallet
                      "0.7.2-SNAPSHOT" :type "test-jar"]
                     [org.cloudhoist/pallet-lein "0.5.1"]]
  :profiles {:dev {:dependencies [[org.cloudhoist/pallet
                                   "0.7.2" :classifier "tests"]]
                   :plugins [[org.cloudhoist/pallet-lein "0.5.1"]]}}
  :local-repo-classpath true
  :repositories
  {"sonatype-snapshots" "https://oss.sonatype.org/content/repositories/snapshots"
   "sonatype" "https://oss.sonatype.org/content/repositories/releases/"})
