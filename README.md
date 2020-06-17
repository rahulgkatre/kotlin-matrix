# Kotlin Matrix

This is some code I am in the process of writing that has the ability to do matrix algebra using Kotlin. This is an evolution of another project of mine where I computed matrix multiplication, determinants, and inverses using Java. I chose Kotlin for this project because of the ability to overload operators, a useful feature when working with some custom data strutures (complex numbers, fractions, vectors, and matrices).

I implemented my own complex number class as my Java versions were not able to work with complex numbers (they used doubles in the main matrix table). In anticipation of complex eigenvalues and eigenvectors, I spent some time implementing a complex number based matrix table. I am using fractions to display rational numbers so I do not need to worry about rounding, but I will make decimals an option in the future, as well as calculating eigenvalues/eigenvectors and more matrix decompositions.

So far, the features of my program include:

- Matrix addition, subtraction, and multiplication

- Augmented matrices and terminal outputs for all included data structures

- Gaussian elimination using scaled partial pivoting to obtain REF and RREF forms

- LU decomposition 

- Inverse (using Gaussian elimination) and determinants (using LU)

Inter-matrix operations (and inter-complex, inter-fraction, etc.) are handled by the basic operators (+, -, *) while other operations are package wide functions that can be called after the line `import matrix.*` has been added to the top of your file and the module has been incorporated into your project. This was done to simplify syntax when calling functions upon matrices. 
