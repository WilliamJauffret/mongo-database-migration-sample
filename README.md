# mongo-database-migration-sample
Sample use case on how to migrate huge production mongo database - Tested and approved in huge company. 

This Sample illustrate how a huge database migration could be performed using code and spring boot. 
In my case it was the cart database, responsible for all addition, deletion, modification, adding items et removing items from the customers cart, also required during the transformation of the cart into order. Very critical database for the business as you might guess. 

This sample has been tested and approved during my last job in a huge retail company that has encounter this issue. 

# The problem to solve : 

- You have a huge mongo database in a very old version.
- You are in a very high business production environment, 99.999% availability is mandatory so you can't stop your product for maintenance, so you can't drop / restore your mongo database in the newer version. In my case it was the cart database, so very critical database for the business.  
- You don't know how many times a drop would take, neither the restore time. 
- You have to upgrade your mongo version because of ne necessity of newer feature available into the target version but not in the actual one (maybe disk space, consumption, optimisation ). 
- You are in a kubernetes environment. 

# The business solution :

So here we are. Now that we are done with the problem to solve let's explain the business approach selected by the developers team and I.

The idea was to set up a new mongo database in the desired version. 
We now have two different database for the same API, the deprecated and the new one. Let's call them mongo_deprecated and mongo_new. 

Now we have this two database up and running, here are the rules to apply in order to smoothly migrate on the new one : 

- All the read query should look into the new database first and then in the old if nothing is founded in the first one. READ : find into mongo_new -> if empty -> find into mongo_deprecated -> if empty return 404 
- All the write query will write into the new database and if configuration on, into the deprecated one also : WRITE : insert into mongo_new -> if we want to maintain deprecated update true -> insert into mongo_deprecated -> Return 201 / 200 

And That's All !Just using two basics rules, the requests/seconds against the old database will smoothly be reduce to zero. And the requests/seconds against the new database will increase in the same time. 

When the old database query/seconds shut down to zero, we will be able to shut it down and disable all rules exposed before. 

# The technical solution : 

We have two majors requirements : 
- This should be completely transparent for all business code above your repository.
- This should be easily configurable and toggleable.

So the idea is to have a deprecatedRepository responsible to CRUD on the mongo_deprecated, and a newRepository responsible to CRUD on the mongo_new. 
Off course this two repo should implements the same interface in order to be completely transparent for the business code above. 

This is **`DepractedCartRepository`** and **`NewCartRepository`**, one for each database.

But we also need some logic to do the necessary switch between the old and the new repo, here is **`SwitcherCartRepository`** here for. As you can see, all the business rule are in the SwitcherRepository. 

As you can see, In order to centralise the read of `mongoDoubleDatabaseMigrationOn` and the logic to apply the new function or de deprecated one, i created the `SmartFunctionExecutor` class. 

You API is now ready to work with two database and smoothly transfer all usage to the new one. All the customers will smoothly have their cart transferred into the new database without any interruptions. 

You now just have to run two mongo database and test it :  `docker run --name mongo_deprecated -d -p 27018:27017 mongo` and `docker run --name mongo_new -d -p 27017:27017 mongo`


