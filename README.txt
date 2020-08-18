This program enumerates Colombian Langford pairings, up to reversal of the order. 

Langford pairings are arrangements of two instances of each of the numbers from 1 to n, such that there are exactly k numbers between the two instances of k, for all k from 1 to n. For example, for n = 3:

3 1 2 1 3 2

Colombian Langford pairings are subject to the additional constraint that there must be exactly one instance of the numbers {1, 2, ..., n-2, n} between the two instances of n-1 (and consequently, one instance of each not between them), so the n=3 example above is also Colombian. This variant of Langford's problem was invented by Bernardo Recamán Santos and Freddy Barrera in Bogotá, hence the name. See https://dialectrix.com/langford/ColombianVariant.html.

The three solutions for n=7 are:

4 1 6 1 7 4 3 5 2 6 3 2 7 5

2 3 6 2 7 3 4 5 1 6 1 4 7 5

7 3 1 6 1 3 4 5 7 2 6 4 2 5 

The program takes two arguments, the first being n, and the second being the size of the cache used to assist the calculation (minimum 1). How large a cache is required is left as an excercise for the reader (the program does report how much of the cache has been used).

To run the program with a large cache it will be necessary to run the program with such VM arguments as:

-d64 -Xmx14g -XX:NewRatio=32

for example.

The program does not attempt to find any pairings where there are fewer than ceiling((n-1-sqrt(n+1))/2) items before the first instance of n-1. This is because such a pairing is impossible.

Proof:

Define P as the set of numbers to the left of the first n-1, or the size of this set, according to context. Similarly define Q as the set of numbers to the right of the second n-1. So P+Q = n-1.

Consider the problem of trying to place P numbers to the left of the first n-1. This is equivalent to the problem of filling P nodes in the following triangular grid, with n rows and columns, such that there is no more than one filled node in each row, column, or diagonal (descending from left to right).


0 - 0 - 0 - ... - 0 - 0 - 0 - 0
  \ | \ | \     \ | \ | \ | \ |
    0 - 0 - ... - 0 - 0 - 0 - 0
      \ | \     \ | \ | \ | \ |
        0 - ... - 0 - 0 - 0 - 0
                          .
						  .
                          .
                  0 - 0 - 0 - 0
                    \ | \ | \ |
                      0 - 0 - 0
                        \ | \ |
                          0 - 0
                            \ |
                              0
							  
The column of a filled node corresponds to the position of a number in the pairing. The row of a filled node corresponds to the number in that position, e.g. 1 corresponds to the bottom row and n to the top row. The diagonal of a filled node corresponds to the position of the matching number between the two instances of n-1, e.g. a node on the long diagonal corresponds to a number whose matching number is immediately to the right of the first instance of n-1. We say that a node is "blocked" if a node in its row, column, or diagonal has been filled (including the node itself). Therefore each node can be blocked up to three times, once horizontally, once vertically, and once diagonally. When a node is filled, it creates 2n+1 "node blocks". For example, if the node at the top right of the grid is filled, it blocks n nodes horizontally, n nodes vertically, and one node diagonally (itself).

Therefore, filling P nodes requires (2n+1)P node blocks to be used.

There are n(n+1)/2 nodes in the array, implying 3n(n+1)/2 available node blocks. However, the number n-1 is not available to use, so the nodes in the second row from the top cannot be blocked horizontally. Furthermore, since we will only be using the last P columns of the grid, the first Q+1 columns cannot be blocked vertically, making another (Q+1)(Q+2)/2 node blocks unavailable. Similarly, there will be Q+1 unused diagonals, making at least another (Q+1)(Q+2)/2 node blocks unavailable. Finally, for every q in Q, the corresponding row (counting from the bottom) cannot be used, because the number is being used at the other end of the pairing. This makes sum(q in Q) horizontal node blocks unavailable.

Since the total number of blocks used cannot exceed the number available, we have:

(2n+1)P <= 3n(n+1)/2 - (n-1) - (Q+1)(Q+2) - sum(q in Q)

Similarly:

(2n+1)Q <= 3n(n+1)/2 - (n-1) - (P+1)(P+2) - sum(p in P)

Clearly, sum(p in P) + sum(q in Q) = n(n+1)/2 - (n-1).

By adding the two inequalities we obtain:

0 <= 5n(n+1)/2 - (P+1)(P+2) - (Q+1)(Q+2) - (2n+2)(n-1)

Substituting for Q and rearranging:

0 <= -2P^2 + 2(n-1)P - (n^2)/2 + 3n/2

=> P >= (n-1-sqrt(n+1))/2, as asserted.





