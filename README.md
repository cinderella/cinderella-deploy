# Cinderella deployment

Cinderella deployment. Deploys a single node running cinderella and vblob.

# Running

Install [leiningen 2](https://github.com/technomancy/leiningen).

In the project root, configure pallet with your username and password:

```
lein pallet add-service bluelock vcloud-ORG USER@ORG PASSWORD vcloud.endpoint https://zone01.bluelock.com/api vcloud.template osFamily=UBUNTU jclouds.vcloud.defaults.network 'internet01-.*'

```

To create the cinderalla node:

```
lein pallet converge cinderella.groups.cinderella/cinderella 1
```

To list the node:

```
lein pallet nodes
```

To remove the node:

```
lein pallet converge cinderella.groups.cinderella/cinderella 0
```

## Testing

To run the live tests add the following in your `~/.lein/profiles.clj` file:

```clj
{:live-test {:jvm-opts ["-Dpallet.test.live=true
                        "-Dpallet.test.service-name=bluelock"]
             :dependencies [[org.jclouds/jclouds-compute
                             "1.5.0-beta.11"]
                            [org.jclouds/jclouds-blobstore
                             "1.5.0-beta.11"]
                            [org.jclouds/jclouds-all
                             "1.5.0-beta.11"]
                            [org.cloudhoist/pallet-jclouds
                             "1.5.0-SNAPSHOT"]]}}
```

adjusting the `service-name` to correspond to the pallet service you have
confiured, and the dependencies to pull in the correct jclouds providers.

Then you can run

    lein pallet with-profiles live-test,default test

## License

Licensed under the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0.html), Version 2.0.
