
# coding: utf-8

# In[506]:

import pandas as pd
path = 'data/training.tsv'
sms = pd.read_table(path, header=None, names=['label', 'message'])


# In[507]:

sms.shape


# In[508]:

sms.head(10)


# In[509]:

sms.label.value_counts()


# In[510]:

sms['label_num'] = sms.label.map({'Lowest':0, 'Low':1, 'Medium':2, 'High':3, 'Highest':4, 'Priority':5})


# In[511]:

X = sms.message
y = sms.label_num
print(X.shape)
print(y.shape)


# In[512]:

from sklearn.cross_validation import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, random_state=1)
print(X_train.shape)
print(X_test.shape)
print(y_train.shape)
print(y_test.shape)


# In[513]:

from sklearn.feature_extraction.text import CountVectorizer
vect = CountVectorizer()


# In[514]:

vect.fit(X_train)
X_train_dtm = vect.transform(X_train)


# In[515]:

X_train_dtm


# In[516]:

X_test_dtm = vect.transform(X_test)
X_test_dtm


# In[517]:

from sklearn.naive_bayes import MultinomialNB
nb = MultinomialNB()


# In[518]:

get_ipython().magic('time nb.fit(X_train_dtm, y_train)')


# In[519]:

# make class predictions for X_test_dtm
y_pred_class = nb.predict(X_test_dtm)
print(y_pred_class)


# In[520]:

from sklearn import metrics
metrics.accuracy_score(y_test, y_pred_class)


# In[521]:


metrics.confusion_matrix(y_test, y_pred_class)


# In[522]:

y_pred_prob = nb.predict_proba(X_test_dtm)[:, 1]
y_pred_prob


# In[ ]:




# In[523]:

# example text for model testing
simple_test = ["One role Lizard Business Analyst only has permissions to the menu Admin-> Configuration and Operation->Data Control, assigned to a user, gives unexpected error occured message when user tries to add new source"]


# In[524]:

simple_test_dtm = vect.transform(simple_test)


# In[525]:

y_pred_class=nb.predict(simple_test_dtm)

print(y_pred_class)


# In[526]:

lines = open('C:\\Users\\Shwait\\pycon-2016-tutorial-master\\data\\file.txt').read().split("\n\n")


# In[ ]:




# In[ ]:




# In[527]:

simple_test_dtm = vect.transform(lines)


# In[528]:

y_pred_class=nb.predict(simple_test_dtm)

print(y_pred_class)


# In[529]:

result= open("C:\\Users\\Shwait\\pycon-2016-tutorial-master\\data\\Result.txt","a+")


# In[530]:

makeitastring = ''.join(map(str, lines))
result.write(makeitastring)


# In[531]:



result.write("\n")
result.write("\n")
result.write("Here is the Priority class for your JIRA ")


result.write("        ")

makeitastring1 = ''.join(map(str,y_pred_class))
result.write(makeitastring1)


result.write("        ")
result.write("\n")
result.write("\n")


# In[532]:

result.close()

