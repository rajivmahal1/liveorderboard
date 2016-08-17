# lob-interview

##1 Implementation Notes

1) Requirement is stated below as demonstrated in OrderBoardAcceptanceTest

2) To get the live order board summaries an order type is required as a parameter (BUY/SELL). Requirements weren't clear.

3) An order does not have an identifying Id (which would normally not be the case) and therefore when merging two orders on price per quantity I have used user Id and quantity to show a breakdown of the merge

4) Concentrated on functionality first. Performance requirements would need to be quantified as well as requirements for concurrency and a strategy for managing it.

##2 Requirement

A 'Live Order Board' with the following functionality:

1) Register an order. Order must contain these fields:
- user id
- order quantity (e.g.: 3.5 kg)
- price per kg (e.g.: £303)
- order type: BUY or SELL

2) Cancel a registered order - this will remove the order from 'Live Order Board'

3) Get summary information of live orders (see explanation below)
Imagine we have received the following orders:
a) SELL: 3.5 kg for £306 [user1]
b) SELL: 1.2 kg for £310 [user2]
c) SELL: 1.5 kg for £307 [user3]
d) SELL: 2.0 kg for £306 [user4]

Our ‘Live Order Board’ should provide us the following summary information:
- 5.5 kg for £306 // order a) + order d)
- 1.5 kg for £307 // order c)
- 1.2 kg for £310 // order b)

The first thing to note here is that orders for the same price should be merged together (even when they are from different users). In this case it can be seen that order a) and d) were for the same amount (£306) and this is why only their sum (5.5 kg) is displayed (for £306) and not the individual orders (3.5 kg and 2.0 kg).

The last thing to note is that for SELL orders the orders with lowest prices are displayed first. Opposite is true for the BUY orders.
