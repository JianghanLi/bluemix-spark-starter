**IBM Analytics for Apache Spark** is a cloud-based data service you can use on Bluemix, IBM’s open cloud platform for building, running, and managing applications.
**Apache Spark** is a distributed data processing analytics engine that makes available new capabilities to data scientists, business analysts, and application developers. Analytics for Apache Spark provides fast in-memory analytics processing of large data sets.
Apache Spark is an open source cluster computing framework optimized for extremely fast and large scale data processing, which you can access via the newly integrated notebook interface IBM Analytics for Apache Spark. You can connect to your existing data sources or take advantage of the on-demand big data optimization of Object Storage. Spark plans are based on the maximum number of executors available to process your analytic jobs. Executors exist only as long as they're needed for processing, so you're charged only for processing done.

Analytics for Apache Spark works with tools, services, and data sources available in IBM Bluemix so that you can quickly start tapping into the power of Apache Spark. The tools include the following:
 - Apache Spark for  data processing at scale.
 - Jupyter Notebooks for interactive and reproducible data analysis and visualization.
 - spark-submit for batch processing with your own applications
 - SWIFT Object Storage for storage and management of data files.
 
 # Bluemix Spark Starter
 
Bluemix Spark Starter is Scala application that demonstrated how to use IBM Analytics based on Apache Spark. 
In this application you can see simple solution how calculate count of word contained in the file. 
This file is stored in the Swift Object Storage. 

The Swift Object Storage, offers cloud storage software so that you can store and retrieve lots of data with a simple API. It's built for scale and optimized for durability, availability, and concurrency across the entire data set. Swift is ideal for storing unstructured data that can grow without bound.
 
For running Bluemix Spark Starter you have to create a new instance of the Apache Spark service from the Bluemix console. While creating your Spark service instance, you have the option to create an Object Storage instance, or to connect to an existing Object Storage instance.

You need to clone https://github.com/Altoros/bluemix-spark-starter.git and change following properties for swift object storage:
 - auth.url (equal auth_url property from Swift Object Storage Service Credentials)
 - username (equal username property from Swift Object Storage Service Credentials)
 - password and apikey (both equal password property from Swift Object Storage Service Credentials)
 - region (equal region property from Swift Object Storage Service Credentials)
 - tenant (equal tenant_id property from Apache Spark Service Credentials)

Build executable jar for bluemix-spark-starter. For this goal you need to install apache maven and run following command under the starter:
mvn clean, package

After that you can found executable jar with Spark job in the _bluemix-spark-starter/target_ folder.

To start running analytics with Analytics for Apache Spark, choose one of the following options:
 - Use notebooks.
 - Run Spark applications by using spark-submit. 
 
###Developing with notebooks
Analytics for Apache Spark leverages the powerful big data processing and analytics capabilities of Apache Spark directly in Jupyter notebooks.

Jupyter Notebooks are a web-based environment for interactive computing. You can run small pieces of code that process your data and immediately view the results of your computation. Notebooks include all of the building blocks you need to work with data: the data, the computations that process the data, visualizations of the results, and text and rich media to enhance understanding.

Use the exploratory method of interactive notebooks where the results of each execution build upon each other to unlock key insights from your data quickly. Notebooks record how you worked with data, so you can understand exactly what was done with the data, even if time has passed since the notebooks were created. This enables you to reproduce computations reliably and share your findings with others.

To run analytics applications in notebooks:
 - Create a new Scala notebook in existent Bluemix Apache Spark service instance
 - In the first cell, enter and run the following special command called AddJar to upload the executable jar to the IBM Analytics for Spark service. Insert the URL of your jar.
For example:
```
%AddJar https://github.com/SergeiSidorov/bluemix-spark-shell/raw/master/com.altoros.bluemix.spark-1.0-SNAPSHOT.jar -f
```
_That % before AddJar is a special command, which is currently available, but may be deprecated in an upcoming release. We'll update this tutorial at that time. The -f forces the download even if the file is already in the cache._

Now that you deployed the jar, you can call APIs from within the Notebook. 
 - Call functions from a Notebook cell
For example:
```
val words = com.altoros.bluemix.spark.WordCount.countOfWord(sc)
words.foreach(p => println(">>> word = " + p._1 + ", count = " + p._2))
```

Final you can see results in your Bluemix Jupyter Notebook.

###Developing application using the spark-submit
You can use the spark-submit.sh script to run applications with your Analytics for Apache Spark instance.
You can bring your own Apache Spark application and run it on the IBM Analytics for Apache Spark for Bluemix service.
Running your own Apache Spark application on the Analytics for Apache Spark service lets you take advantage of powerful on-demand processing in the cloud. And you can load your own data into an object store in Bluemix for fast, cost-effective access.

The spark-submit.sh script performs the following functions:
 - Runs the Apache Spark spark-submit command with the provided parameters.
 - Uploads JAR files and the application JAR file to the Spark cluster.
 - Submits a POST REST call to the Spark master with the path to the application file.
 - Periodically checks the Spark master for job status.
 - Downloads the job stdout and stderr files to your local file system.

Restriction: Running the spark-submit.sh script is supported on Linux only.

To run a Spark application using the spark-submit.sh script:
 - Create folder where you plan to run app and put executable jar into this folder
 - Create an instance of the Analytics for Apache Spark service on IBM Bluemix, and record the credentials from the Service Credentials page.
 - Copy the service credentials from your Spark instance to a file named vcap.json in the directory where you plan to run the spark-submit.sh script. 
 For example:
 ```
 {
   "credentials": {
     "tenant_id": "s1a9-7f40d9344b951f-42a9cf195d79",
     "tenant_id_full": "b404510b-d17a-43d2-b1a9-7f40d9344b95_9c09a3cb-7178-43f9-9e1f-42a9cf195d79",
     "cluster_master_url": "https://169.54.219.20:8443/",
     "instance_id": "b404510b-d17a-43d2-b1a9-7f40d9344b95",
     "tenant_secret": "8b2d72ad-8ac5-4927-a90c-9ca9edfad124",
      "plan":"ibm.SparkService.PayGoPersonal"
   }
 }
 ```
 - Download the Analytics for Apache Spark spark-submit.sh script, which you will use to run your application. 
 Download the script from https://github.com/Altoros/bluemix-spark-starter.git
 - Run the spark-submit.sh script.
For example:
```
./spark-submit.sh \
--vcap ./vcap.json \
--deploy-mode cluster \
--class com.altoros.bluemix.spark.WordCount \
--master https://169.54.219.20:8443 \
com.altoros.bluemix.spark-1.0-SNAPSHOT.jar
```

Log files stdout and stderr are written to your local file system. The log file names include a timestamp to identify each file. The timestamp is identical for all output from each time you run the spark-submit.sh script.
For example:
```
stdout_1458677079521707955 or stderr_1458677079521707955
```


For more information about IBM Analytics for Apache Spark please see following page:
https://console.ng.bluemix.net/docs/services/AnalyticsforApacheSpark/index.html