<html>
    <head>
    <title>Project 4</title>
    <meta charset="utf-8" />
    <style type="text/css">
        body
        {
            margin: 0;
            height: 100%;
            background-color: #262727;
            font-size: x-large; 
            font-family:calibri;
            color:#faf5ed;
        }

        p
        {
            text-align: center;
            font-size: medium;
        }
    </style>
    </head>

    <body>
        <br>

        <h1 style="text-align: center">
            Welcome to the Fall 2019 Project 4 Enterprise System
        </h1>

        <h3 style="text-align: center">
            A Remote Database Management System
        </h3>
        <hr>
        <br>

        <p> You are connected to the Project 4 Enterprise System Database.</p>
        <p> Please enter any valid SQL query or update statement.</p>
        <p> If no query/update command is initally provided, the execute button will display all supplier information in the database.</p>
        <p> All execution results will appear below.</p>

        <form style="text-align: center" action = "database" method = "post">
        <textarea style="background-color: #262727; color:#faf5ed;" name="sql" rows="15" cols="100"></textarea>
        <br>
            <input type = "submit" value = "Submit" />
            <input type = "reset" value = "Clear Form" />
        </form>

        <hr>
        <p> Database Results: </p>
        <p> empty </p>

    </body>
</html>
