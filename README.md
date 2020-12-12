# Requirement
*My working environments is as follows:*
- maven 3.2.5
- jdk 1.8.0_152
- path is correctly configured
# How to run
- Modify configurations in file *application.properties*
- Source run.sh in the root directory
# Design
- The ***App*** class is the entrance and is driven by the ***OrderDispatcher*** class (has it's own thread), it reads orders from ***OrderFactory*** then dispatches to ***Kitchen***. It waits, then quits until all orders are processed (delivered or discarded or rotted).
- When the ***Kitchen*** receives an order, it tries to put the order on the right hot/cold/frozen shelf (temperature matches). If that is not possible, then places on the overflow shelf. If the overflow shelf is full, then discard the rottenest order.
- Each shelf has it's own thread that scans the orders it holds, delivers the order when the courier is ready or discard the order when it is considered as rotten (a calculated value less or equal than zero).
- Plus that, after each scan loop of the overflow shelf, it moves orders to hot/cold/frozen shelf whenever possible (starts from the rottenest order).
# Others
- There's no test case because I was too busy that had little time working on this great homework. Sorry for that.
- Contact me via phasefei@foxmail.com if you would like to.
