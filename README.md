# JagexCodingExercise

Jagex Coding Exercise
Please find the description below on building a complete RESTful API using different HTTP methods.

•	GET – to retrieve and search data
•	POST – to add data
•	PUT – to update data
•	DELETE – to delete data

I have implemented this API using JAX-RS and Jersey. Consuming these services using JQuery and a simple DAO for the data access solution.

Here are the JAX-RS annotations used in “PackageResource.java”

•	@GET, @POST, @PUT, @DELETE are the http methods.

•	@Path – used to define URI matching pattern for the incoming http requests.

•	@Consumes - type of data the method can take as input. The data will automatically be deserialized into a method input parameter. For example, you can pass a package object to the addPackage() method either as JSON or XML. The JSON or XML representation of a new package is automatically deserialized into the Package object passed as an argument to the method.

•	@Produces - One or more response content type(s) the method can generate. The method’s return value will be automatically serialized using the content type requested by the client. If the client didn’t request a specific content type, the first content type listed in the @Produces annotation will be used. For example, if you access rest/packages, you get a list of packages represented as JSON because it is the first content type listed in the @Produces annotation of the findAll() method.

The JQuery client sends the data to the server using JSON (addPackage() and updatePackage()) methods.
