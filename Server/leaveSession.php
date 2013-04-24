<?php
/*
leaveSession - removes a user from a session
Params: Session number, (token)
Returns: success - user was removed
         error - user could not be removed 
                 (user was not enrolled in the first place?)
*/
include 'getUser.php';

$token = $_GET["token"];
$session_id = $_GET["session_id"];

$user_id = getUserFromToken($token);

//query for session password
$query = "DELETE FROM in_session WHERE user='$user_id' AND session='$session_id'";

$result = $mysqli->query($query);

if($result==false)
	{
    echo FAILURE;
		return false;
	}
else 
  {
    echo SUCCESS;
  }

?>