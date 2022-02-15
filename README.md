

## API Mock Framework
A Hosted WireMock Framework whose premise was originally devised from the Industry Version of WireMock. 

One that will provide a library of Microservices catered towards New MicroService API Development.  The advantage to integrating and implementing the WireMock Framework is a pro-active measure.  

For example,  lets say your project has just kicked-off.  As you can imagine your team of developer resources are anxious to get started; however many of the project dependencies require some level "red-tape", governance, and or security to gain access to them.  Or maybe your Projects' API Dependencies are simply not available at this time.  Maybe there has been an unscheduled Network Outage.  The point of the matter is the API WireMock Framework may assist.  

As of this writing, 02/15/2022, within the current framework is a subset of MOCK API's, to act as a guide or example.  We offer helper methods, such that you merely need to add your new Mock API Services to the '__file' & 'mapping' locations beneath this Project's Resources Folder.
So you might find yourself asking, How do I get Started?"

## Before We Continue, A Little History ...

The API Mock Framework is a basic version that imitates or mirrors the Industry delivered WIREMOCK Framework found at the following sites:

|-|Site |Address |
|-|-----|--------|
|1|WIREMOCK | http://wiremock.org/ |
|2|WIREMOCK - GiTHub | https://github.com/wiremock |
|3|Spring Cloud | https://cloud.spring.io/spring-cloud-contract/reference/html/project-features.html#features-wiremock |

Creating an equivalent of Industry's WIREMOCK, was a challenge of mine.  One that was presented to me professionally and personally.  Reading through the Terms and Conditions of the Industry version of WIREMOCK, this could be seen as a breach of such terms.  Please make certain you read the T&C prior to consuming this codebase.  Originally the hope was to deploy the Industry Version of WIREMOCK and HOST it as a StandAlone.  Unfortunatley, in doing so, I encountered several issues along the way.  They are as follows:

|-|Issues |
|-|-------|
|1|The Industry version of the WIREMOCK Framework comes packaged with it's own Server. |
|2|Spring Boot Framework comes packaged with it's own Server. |
|3|Competing Servers Instances within the same Servlet Container impeded MOCK API Traffic OR Traffic was simply lost. |
|4|Configuring the Web Application Project to drop the WIREMOCK Integrated Server Instance resulted in missing Technology Dependencies. |
|5|The Industry version of the WIREMOCK Framework requires both "__files" and "mapping" sub-folders to be at the ROOT LEVEL. |

Long Story Short, to quote an Industry WIREMOCK Framework Developer Resource, it truly was meant to be leveraged as a Stand-Alone Utility at the Command Line. 

## Prerequisites...
In order to spin up the API Mock Framework the following technologies were leveraged:

|-|Technologies |
|-|-------|
|1|Eclipse Integrated Development Environment(IDE) - 2020-03 |
|2|JFROG Artifactory - Remote Repos URL |
|3|Spring Framework - SpringBoot |
|4|Spring Framework - SpringBoot - Starter Web |
|5|Spring Framework - SpringBoot - Starter Tomcat |
|6|Spring Framework - SpringBoot - Configuration Processor |
|7|Spring Framework - SpringBoot - Starter Security |
|8|Spring Framework - SpringBoot - Starter Actuator |
|9|Spring Framework - SpringBoot - Security Config |
|10|Spring Framework - SpringBoot - Security Core |
|11|Spring Framework - SpringBoot - Security Web |
|12|Spring Framework - Spring OXM |
|13|IO Micrometer - Micrometer Registry Prometheus |
|14|FasterXML - Jackson DataBind |
|15|FasterXML - Jackson Core |
|16|ORG JSON - 20210307 |
|17|Adopt JDK - Version 1.8 |
|18|JUNITS |
|19|GiTHub / GiTHub Desktop |
|20|Maven / Gradle / Gradlew |
|21|SonarQube |
|22|Jenkins Pipeline |
|23|Groovy |

## Getting Started...
To **NOTE**, it is left to the reader to have a technical background, to be considered a developer resource, who firmly understands how to import and or standup an application, into a Eclipse Integrated Development Environment(IDE) and or your Preferred IDE.

In mirroring Industry's Version of WIREMOCK, we maintained the API Architecture Dependencies, Folder/File Naming Conventions, JSON Format, as well as introduced a couple of Administrative API Calls, for Developer Ease.   The only variance, outside of the WireMock Framework now being HOSTED in Lower Environments, is that those Architecture Dependencies can now be found beneath the Project Application **"Resources"** Folder.  In order for a Developer Resource to **Add** and or **Update** a MOCK API, there are **ONLY Two Key Areas** to Consider. No other area of this application should require refactoring.

>> If however you do find yourself refactoring other areas of this application, please reach out to me, I will do the best I can to assist you and reply as promplty as possible. 

Those **Two(2) Key Areas** ( API Architecture Dependencies ) are as Follows:
1.  "**__files**"
2. "**mapping**"

In order for the Developer Resource to effectively work with these **Two(2) Key Areas** a Number of Guidelines must be considered. 

#### API MOCK Framework Guidelines
1.  The MOCK API "**mapping's**" and their associative "**__files**" are in a JSON Format. 
      a.  This format must be maintained, otherwise JSON Parsing Exceptions will be Thrown
    
2.  The MOCK API File Naming Conventions are unique to the MOCK API Route in Question.
     a.  For example, lets say we want to create a "Sales-Rep" MOCK API. 
     
          i.    The Naming Convention for the "Mapping" File must be "sales-rep-mapping.json". 
          
          ii.   The "__files" Naming Convention is a little more flexible.  Its File Name is 
               declared within the "Mapping" File.  However to adhere to a level of consistency,  
               it's Naming Convention should resemble the following;  "sales-rep.json".
                
          iii.  The only Exception To The Rule here is when MOCK API Routes will have 
                Parameterization Injected.  In other words Arguments Passed within the 
                API URL Route itself.
                
                For example, lets say our "Sales-Rep" MOCK API wants to  Update a Sales Rep.
                The API Route is the following; /v1/sales-reps/{code}
                Our File Naming Conventions will be the following:
                1.  Mapping: sales-rep-mapping.json
                2. __file:   sales-rep.json

#### Description of Mapping
Provides both HTTP Request & Response Behavior, for the Mocked API.  Presently the "Mapping" File is capable of configuring the HTTP Request & Response Object, in the following manner.
| Object | Mapped Field | Description |
|--|--|--|
|Request |-|-|
|-| url| The MOCKED API Route|
|-| method| The MOCKED API HTTP METHOD {GET, POST, PUT, DELETE}|
|-| bodyPatterns| The MOCKED API HTTP Request Body|
|Response|-|-|
|-| status| The MOCKED API HTTP Status to be returned|
|-| bodyFileName| The MOCKED API HTTP Response Body to be Returned.  This is represented as a filename located within the "__files" Resource Folder.|

It is worth noting, presently an HTTP Request Body can be provided for HTTP POST & PUT Methods, however no HTTP Request Validation is taking place, for such body.  Only the URL, Method, and bodyFileName Fields are being validated, to determine the appropriate MOCK API Traffic.  A Mapping Code Snippet Example Follows.

    {
      "request": {
         "url": "/v1/sales-reps",
         "method": "POST",
         "bodyPatterns": [
           {
             "equalToJson": {
               "id": "12345",
               "channel": "Channel Name",
               "manager": {
                  "id": "678910",
                  "firstName": "Bob",
                  "lastName": "Marley",
                  "phoneNumber": "3125882300",
                  "email": "sales.rep@email.com"
            },
            "position": "floor",
            "salesRepAssigned": {
               "id": "11121314",
               "firstName": "Fred",
               "lastName": "FlintStone",
               "phoneNumber": "3128675309",
               "email": "sales.rep@email.com"
            },
            "states": [
              "string"
            ],
            "status": "Active",
            "territory": "EAST C (EC)",
            "code": "223344"
            }
          }
         ]
       },
       "response": {
         "status": 201,
         "bodyFileName": sales-reps.json
       }
     }
      
#### Description of __files
As described earlier within the **Mapping** Section, "**__files**" is the Project Application Resource Location where the corresponding MOCK API HTTP Response Body lives.  The "**Mapping**" Field "**bodyFileName**" makes reference to what the configured HTTP Response Body should be.  In the Code Snippet Example above, a filename was provided, "**sales-reps.json**".  In essence, the MOCK API "**Sales Rep**" will have a configured HTTP Response Body whose content, is found within the , "**sales-reps.json**" file.  And example of such content follows;

    {
      "salesRepNumber": "9988776655"
    }
   
#### Administrative API Calls
There are currently Two WireMock API Calls that can be made which allows the Developer Resource to better understand what is available to them.
1.  /wmf/__admin/mappings
	a.  Returns a List of WireMock Mappings
	
2. /wmf/__admin/routes
	a.  Returns a List of WireMock API Routes
   
#### Conclusion
That my dear reader is basically it.  All that remains is simply re-building the Gradle/Java Application and validating within POSTMAN, or your favorite API Validation Utility.

If there are any questions please feel free to reach out the following resources referenced below.  If you desired a POSTMAN Collection, that can also be provided.


