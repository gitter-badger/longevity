# clean out old version:

rm -rf /usr/local/var/mongodb/cfg0/*
rm -rf /usr/local/var/mongodb/cfg1/*
rm -rf /usr/local/var/mongodb/cfg2/*
rm -rf /usr/local/var/mongodb/a0/*
rm -rf /usr/local/var/mongodb/a1/*
rm -rf /usr/local/var/mongodb/a2/*
rm -rf /usr/local/var/mongodb/b0/*
rm -rf /usr/local/var/mongodb/b1/*
rm -rf /usr/local/var/mongodb/b2/*

# start config servers:

mongod --configsvr --port 26050 --logpath /usr/local/var/log/mongodb/cfg0.log --logappend --dbpath /usr/local/var/mongodb/cfg0 --fork --bind_ip 0.0.0.0
mongod --configsvr --port 26051 --logpath /usr/local/var/log/mongodb/cfg1.log --logappend --dbpath /usr/local/var/mongodb/cfg1 --fork --bind_ip 0.0.0.0
mongod --configsvr --port 26052 --logpath /usr/local/var/log/mongodb/cfg2.log --logappend --dbpath /usr/local/var/mongodb/cfg2 --fork --bind_ip 0.0.0.0

# config the repl sets:

mongod --shardsvr --replSet a --dbpath /usr/local/var/mongodb/a0 --logpath /usr/local/var/log/mongodb/a0.log --port 27000 --fork --logappend --smallfiles --oplogSize 50 --bind_ip 0.0.0.0
mongod --shardsvr --replSet a --dbpath /usr/local/var/mongodb/a1 --logpath /usr/local/var/log/mongodb/a1.log --port 27001 --fork --logappend --smallfiles --oplogSize 50 --bind_ip 0.0.0.0
mongod --shardsvr --replSet a --dbpath /usr/local/var/mongodb/a2 --logpath /usr/local/var/log/mongodb/a2.log --port 27002 --fork --logappend --smallfiles --oplogSize 50 --bind_ip 0.0.0.0
mongod --shardsvr --replSet b --dbpath /usr/local/var/mongodb/b0 --logpath /usr/local/var/log/mongodb/b0.log --port 27010 --fork --logappend --smallfiles --oplogSize 50 --bind_ip 0.0.0.0
mongod --shardsvr --replSet b --dbpath /usr/local/var/mongodb/b1 --logpath /usr/local/var/log/mongodb/b1.log --port 27011 --fork --logappend --smallfiles --oplogSize 50 --bind_ip 0.0.0.0
mongod --shardsvr --replSet b --dbpath /usr/local/var/mongodb/b2 --logpath /usr/local/var/log/mongodb/b2.log --port 27012 --fork --logappend --smallfiles --oplogSize 50 --bind_ip 0.0.0.0

# set up two mongos instances:

mongos --configdb Johns-MacBook-Pro-2.local:26050,Johns-MacBook-Pro-2.local:26051,Johns-MacBook-Pro-2.local:26052 --fork --logappend --logpath /usr/local/var/log/mongodb/mongos0.log --bind_ip 0.0.0.0
  
mongos --configdb Johns-MacBook-Pro-2.local:26050,Johns-MacBook-Pro-2.local:26051,Johns-MacBook-Pro-2.local:26052 --fork --logappend --logpath /usr/local/var/log/mongodb/mongos1.log --bind_ip 0.0.0.0 --port 26061

# configure replica sets:

mongo --port 27000 --eval 'rs.initiate()'
mongo --port 27000 --eval 'rs.add("Johns-MacBook-Pro-2.local:27001")'
mongo --port 27000 --eval 'rs.add("Johns-MacBook-Pro-2.local:27002")'

mongo --port 27010 --eval 'rs.initiate()'
mongo --port 27010 --eval 'rs.add("Johns-MacBook-Pro-2.local:27011")'
mongo --port 27010 --eval 'rs.add("Johns-MacBook-Pro-2.local:27012")'

# add shards:

mongo --eval 'sh.status()'
mongo --eval 'sh.addShard("a/Johns-MacBook-Pro-2.local:27000")'
mongo --eval 'sh.addShard("b/Johns-MacBook-Pro-2.local:27010")'
