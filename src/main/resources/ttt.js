db.getCollection('metricsByMin').find({})



//Check for the existence of a doc in the collection for the passed

//servicename,ip,port  and _id combination

db.getCollection('metricsByMin').find(

{

    "_id" : ObjectId("57b9d2b9a6ba18cb03091931"),

   "servicename" : "billing",

    "ip" : "30.122.32.138",

    "port" : "8051"

 

},

{

    "metrics.type": 1

}

)

//If above query does not returns rows then doc does not exist, so add a new document



//TODO



db.getCollection('metricsByMin').insert({

   "servicename" : "billing",

    "ip" : "30.122.32.138",

    "port" : "8051",

    

     "metrics" : {

         "type":"tesr",

        "valuesQ1" : [ 

            200

        ],

        "valuesQ2" : [],

        "valuesQ3" : [],

        "valuesQ4" : []

    }

})





//If above query returns rows then doc exists , but type may or may not exist

//Read the metrics.type returned in the response to find out if the type exists or not in the document



//If the passed type is present in the document then update the specific quarter array field accordingly

db.getCollection('metricsByMin').update(
    {
   "_id" : ObjectId("57b9da9da6ba18cb03091936"),

    "servicename" : "billing",

    "ip" : "30.122.32.138",

    "port" : "8051",

    "metrics.type":"tesr"
    },
    // update 
    {

     //Determine which quartervalue field to update based on the second received in the request

     //and set the value accordingly

    $push: { "metrics.$.valuesQ1": 215 } 

    },
    
    // options 
    {
        "multi" : false,  // update only one document 
        "upsert" : true  // insert a new document, if no existing document match the query 
    }
);

    

//If doc exists but type does not exist then

    

    db.getCollection('metricsByMin').update(

    {

    "_id" : ObjectId("57b9da9da6ba18cb03091936"),

    "servicename" : "billing",

    "ip" : "30.122.32.138",

    "port" : "8051"

    },

    // update 

    {

     $push: {

                metrics: 

              {

                  "type":"ok",

                  //Determine which quartervalue field to update based on the second received in the request

                  //and set the value accordingly

                   "valuesQ1": [200],

                  "valuesQ2": [],

                  "valuesQ3": [],

                  "valuesQ4": [] 

              }

             

            }

    

     },

    // options 

    {

        "multi" : false,  // update only one document 

        "upsert" : true  // insert a new document, if no existing document match the query 

    }

);

    

    
