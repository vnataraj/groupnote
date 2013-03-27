<?php
include 'database.php';
include 'error.php';
function getUserFromToken($token) //returns user ID based on the token
	{
		if(validate($token)==-1){
			error_match("USER_NOT_FOUND");
			exit;
		}
		$result = $mysqli->query("SELECT user_id FROM users WHERE token=$token"); 
		if($result)
			{
				return $result;
			} 
			else
			{
				error_match("unknown");
			}
	}
?>