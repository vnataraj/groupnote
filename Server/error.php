<?php
function error_match($error_code){
	if(strcmp($error_code,"NEW_USER_FAILURE")==0)
	{
		echo "user was not able to be added to the table, please try again!";
	}
	else if(strcmp($error_code,"USER_NOT_FOUND")==0)
	{
		echo "user was not able to be located inside the table, please try again!";
	}
	else if(strcmp($error_code,"NEW_NOTE_FAILURE")==0)
	{
		echo "note was not able to be added to the table, please try again!";
	}
  else if (strcmp($error_code,"CREATE_SESSION_FAILURE")==0)
  {
    echo "session was not able to be created, please try again!";
  }
  else if (strcmp($error_code,"ENROLL_USER_FAILURE")==0)
  {
    echo "User could not be enrolled in session. Try again?";
  }
  else if (strcmp($error_code,"SESSION_INVALID_PASSWORD")==0)
  {
    echo "The password does not match what we have for the session. Try again!";
  }
  else if (strcmp($error_code,"LOGIN_ERROR")==0)
  {
    echo "The user could not be logged in. Try again.";
  }
  else if (strcmp($error_code, "EDIT_NOTE_FAILURE")==0)
  {
	  echo "The note in question was not able to be modified, please try again or seek assistance!";
  }
	//add other error codes here as 'else if's...
	else
	{
		echo "unknown error, we have no idea what happened.  please change notetaking services.";
	}
}

?>