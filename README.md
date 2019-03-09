# CST2335
For Android Course Group Project

A> File Packaging Rules:
1. Java related files:
   a. There are 4 folders named by our names. 
   b. Please write all your Java related code (such as activities, Java Class files, etc.) under your name.
2. Layout xml files:
   a. All our layout.xml files will be under "Layout" folder. 
   b. There is no sub-folders under it. 
   c. In order to make our project looks more organize, please make sure you follow the File Naming Rules as below.

B> File Naming Rules:
Definition: 
a) Post-fix: your xml file/attribute name + "_the initial of your name". The puroose of post-fix is to avoid the "name conflicts" when we are integrate our codes.
   For example: a result xml file for Ryan Li will be "result_rl.xml" and for Kevin Nghiem will be "result_kn.xml";
                an attribute name in strings.xml called "name_hint" for Ryan Li will be "name_hint_rl" and for Kevin Nghiem will be "name_hint_kn"

1. layout xml related files: Please follow the post-fix rule mentioned above. 
2. the attribute name in the strings.xml: please follow the post-fix rule. 
2. Java related files: because they are under our own folder, so no rules for Java related files. 

C> Major files explaination:
1. There are 2 kinds of "Main Activity" xml files.
   (Please DO NOT change their names, but you can create other non-main-activity xml files following post-fix rules): 
      a) App home page: which is "activity_main.xml"
      b) Module Home page: 
         it's the real main activty xml for each module we are working on. 
         eg. when user click on "Merriam Webster Dictionary" button at the app home page, it will jump to "activity_main_dictionary.xml".
2. AndroidManifest.xml: it's the config file for our app
3. strings.xml: there are already 2 more strings.xml under values-zh-rCN (for Chinese language) and values-en-rGB (For UK English). 
4. drawable folders: there are already 3 flag.jpg under drawable-zh-rCN (for Chinese language) and drawable-en-rGB (For UK English). 
