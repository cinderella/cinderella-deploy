# Cinderella demo

Cinderella deploy demo. Deploys a single node running cinderella and vblob.

# Running

Install [leiningen](https://github.com/technomancy/leiningen).

In the project root, configure pallet with your username and password:

```
lein pallet add-service bluelock "username" "password"
```

To create the cinderalla node:

```
lein pallet converge cinderella-demo.groups.cinderella-demo/cinderella-demo 1
```

To list the node:

```
lein pallet nodes
```

To remove the node:

```
lein pallet converge cinderella-demo.groups.cinderella-demo/cinderella-demo 0
```


## License

Licensed under the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0.html), Version 2.0.
