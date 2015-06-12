### Problem ###
Our project will address the following problem: given an academic paper or any general textual query, find a researcher who would be best suited to talk to you about it (based on their previous research). For example, given a paper as a pdf file, we would like to know who in the UW Computer Science department would be best suited to review the paper. Or given a phrase 'probabilistic motion planning', who are the people to talk to in the top Computer Science departments in the US?

### Solution ###
In order to answer this question we will build a statistical profile for a list of researchers based on their published papers, and do a cosine similarity between the query and the profiles to find the best match. Our program will roughly consist of the following modules:

Obtain the data and do some basic processing

Given a web URL address of a person's web page, download all the publications of that person (in pdf format)
Convert the pdf file to a text file
Clean up the text: Appropriate text cleaning like tokenization, removing stop words, and maybe stemming can be done.
Repeat the above procedure for a list of web addresses of multiple researchers' web pages (perhaps given in a file).
Build a bag-of-word statistical profile for each researcher
Given a string, pdf file or a text file as a query, transform it into appropriate format, calculate cosine similarity of the document to each of the profiles, and output a sorted list of best matches.
Consider other applications on the statistical profiles, such as clustering people doing similar work.