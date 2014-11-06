6375project
===========
Develop a Spring based web services with three operations, using SQLite for data persistence 
Implement a contract-first Spring web service. It should have three operations:
execute, that takes in the input data for a complex computational task, and returns the result when the computation is done. If you don't any other proposal, please use t-test or ANOVA analysis from geWorkbench project as the computational task.
submit, that takes the same input but return the an integer job ID immediately without blocking for the computation to finish. (This is where you would need data persistence.)
query, that takes an integer job ID as input, and returns the actual output data, or a status report to indicate that the computation is not finished.
Note that geWorkbench analysis like t-test or ANOVA can be deployed as AXIS2 web services. You can compare the difference if you are interested, but it is not needed for the project.
