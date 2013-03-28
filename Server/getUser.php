<?php

include 'database.php';
include 'error.php';

function getUserFromToken($token) //returns user ID based on the token
	{
		//if(validate($token)==-1){
		//	error_match("USER_NOT_FOUND");
		//	exit;
		//}
    
    global $mysqli;
    
		$result = $mysqli->query("SELECT user_id FROM users WHERE token='$token'"); 
		if($result)
			{
        $row = $result->fetch_assoc();
        return $row['user_id'];
			} 
			else
			{
				error_match("USER_NOT_FOUND");
        return -1;
			}
	}
?>