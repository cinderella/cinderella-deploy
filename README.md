# Cinderella deployment

Cinderella deployment. Deploys a single node running cinderella and vblob.

# Running

Install [leiningen 2](https://github.com/technomancy/leiningen).

In the project root, configure pallet with your username and password:

```
lein pallet add-service bluelock-zone01 vcloud USER@ORG PASSWORD vcloud.endpoint https://zone01.bluelock.com/api vcloud.template osFamily=UBUNTU jclouds.vcloud.defaults.network 'internet01-.*'

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


## License

Licensed under the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0.html), Version 2.0.
