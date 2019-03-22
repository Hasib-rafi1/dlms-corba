# Distributed Library Management System (DLMS) using Java IDL (CORBA)
CONCORDIA UNIVERSITY | DEPARTMENT OF COMPUTER SCIENCE AND SOFTWARE ENGINEERING | Assignment 2 | COMP 6231, Winter 2019 |Distributed Library Management System (DLMS) using CORBA

This is the Corba project of the previous project. and all the infomrmation of project description is from the first assignment :
https://github.com/Hasib-rafi1/dlms

In this assignment, you are going to implement a Distributed Library Management System (DLMS) from Assignment 1 in CORBA using Java IDL. 
In addition to the 3 student operations (namely- borrowItem, findItem, and returnItem) introduced in Assignment 1, the following operation 
needs to be implemented.

● exchangeItem (studentID, newItemID, oldItemID)
  A student might invoke this operation to change an item she/he has already borrowed with another item in any library. In this case, the 
  current_library_server (which receives the request from the user) first checks whether the user has borrowed the old item, then checks with 
  the new_library_server (from which the new item has to be borrowed) whether this new item is available, and if both checks are successful 
  then atomically lends the new item to the user and returns the old item. That is, borrow and return operations should both be successful 
  or none of them should be done. Note that all these checks, borrow and return operations should be done using UDP/IP messages as they are
  server-to-server communications.
 
 In this assignment you are going to develop this modified application in CORBA using Java IDL. Specifically, do the following:
● Write the Java IDL interface definition for the modified DLMS with all the 7 specified operations.

● Implement the modified DLMS. You should design a server that maximizes concurrency. In other words, use proper synchronization that allows 
  multiple users to correctly perform operations on the same or different records at the same time.
  
● Test your application by running multiple clients with the 3 servers. Your test cases should check correct concurrent access of shared data,
  and the atomicity of exchangeItem operation.
  
Your submission will be graded for correct and efficient implementation of all the operations in addition to correct use and implementation of
mutual exclusion in accessing shared data and proper exploitation of concurrency to achieve high performance.
