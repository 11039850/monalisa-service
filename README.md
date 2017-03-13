# monalisa-service
Simple database http serivce. 

## Step 1: Write an interface

```java 

	@DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root", 
	    dbs="testdb", dbsAuthUsers="none")
    public interface TestDB {
    	public static DBConfig DB=DBConfig.fromClass(TestDB.class); 
    }
```

## Step 2: Start web server

## Step 3: Access database as following

### Get all tables:
```
  curl -X HEAD http://localhost:8080/your_web_app/dbs/testdb
```
OR (Use parameter: method=HEAD )
``` 
  curl http://localhost:8080/your_web_app/dbs/testdb?method=HEAD
```

### Get table metadata:
```
  curl -X HEAD http://localhost:8080/your_web_app/dbs/testdb/your_table_name
```

### Select:
```
  curl http://localhost:8080/your_web_app/dbs/testdb/your_table_name
```
OR (Select multi-tables), click [here](https://github.com/11039850/monalisa-service/wiki/Select) for more query parameters. 
```
  curl http://localhost:8080/your_web_app/dbs/testdb/table1,table2/table1.id=table2.id
```
### Insert:
```
  curl -X POST -d "name=zzg&password=123456" http://localhost:8080/your_web_app/dbs/testdb/your_table_name
```
OR (Insert multi-tables)
```
  curl -X POST -d "table1.name=zzg&table2.name=xxx" http://localhost:8080/your_web_app/dbs/testdb/table1,table2
```

### Update:
```
  curl -X PUT -d "name=zzg&password=1" http://localhost:8080/your_web_app/dbs/testdb/your_table_name/id
```
OR (Update by multi-keys)
```
  curl -X PUT -d "name=zzg&password=1" http://localhost:8080/your_web_app/dbs/testdb/your_table_name/id1=1,id2=2
``` 

### Delete:
```
  curl -X DELETE http://localhost:8080/your_web_app/dbs/testdb/your_table_name/id
``` 
OR (Delete by multi-keys)
```
  curl -X DELETE http://localhost:8080/your_web_app/dbs/testdb/your_table_name/id1=1,id2=2
``` 

# Maven: 
```xml
	
	<dependency>
		<groupId>com.tsc9526</groupId>
		<artifactId>monalisa-service</artifactId>
		<version>2.0.0</version>
	</dependency>
``` 