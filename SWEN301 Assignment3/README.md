Jacoco: appender 82%, stats 94%, logserver 82%, logevent 63%

Jdepend:There are no two dependencies that reply on each other in the server. No cycles. This is good.

Spot Bugs:
1. nz.ac.vuw.swen301.assignment3.server.StatsServer.doGet(HttpServletRequest, HttpServletResponse)
 may fail to clean up java.io.OutputStream on checked exception
 
 The output stream is not closed properly and can cause issues in the program.
 
2.Found reliance on default encoding in nz.ac.vuw.swen301.assignment3.server.LogServer.doPost(HttpServletRequest,
 HttpServletResponse): new java.io.InputStreamReader(InputStream)
 
 The charset is not specified in the input stream and the default charset is prone to issues and bugs.
 
3. nz.ac.vuw.swen301.assignment3.server.StatsServer.doGet(HttpServletRequest, HttpServletResponse) may fail to
 clean up java.io.OutputStream on checked exception
 
 The output stream is not closed properly and may fail to clean the output stream and cause issues.