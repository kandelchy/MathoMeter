package com.swipe;

import java.util.Random;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity 
implements OnClickListener { 
            

	private TextView leftAnswer;
	private TextView middleAnswer;
	private TextView rightAnswer;
	private TextView equationLine;
	private TextView playerScore;
	//private TextView shooter;
	private boolean shootFlag;
	private CountDownTimer countDownTimer;

	private boolean timerHasStarted = false;
	private int noOfTries;
	private Button next; 
	private int rightPosition;
	public TextView text;
	
    // define level constants
    private final int LEVEL_EASY = 0;
    private final int LEVEL_MODERATE = 1;
    private final int LEVEL_HARD = 2;
	
    // define type constants
    private final int TYPE_MULTIPLY = 0;
    private final int TYPE_DIVIDE = 1;
    private final int TYPE_MIXED = 2;    
    
	// set up preferences
    private SharedPreferences prefs;
    
    private int levelOfGame = LEVEL_EASY;
    private int timeInSec = 9000;
    private int typeOfGame = TYPE_MULTIPLY;
    
	float x1,x2;
    float y1, y2;
    float screenWidthThird;
    int score;
    int conScore;
    int totalTries = 10;
    
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
            	
    	leftAnswer = (TextView) findViewById(R.id.LeftAnswer);
    	middleAnswer = (TextView) findViewById(R.id.MiddleAnswer);
    	rightAnswer = (TextView) findViewById(R.id.RightAnswer);
    	equationLine= (TextView) findViewById(R.id.Equation);
    	playerScore = (TextView) findViewById(R.id.playerScore);
    	next =(Button)findViewById(R.id.next);
    	next.setVisibility(View.GONE);
    	text = (TextView) this.findViewById(R.id.timer);
    	
    	//Typeface face = Typeface.createFromAsset(getAssets(),
        //        "fonts/oneway.ttf");    	
    	
    	//leftAnswer.setTypeface(face);
    	//middleAnswer.setTypeface(face);
    	//rightAnswer.setTypeface(face);
    	//equationLine.setTypeface(face);
    	
    	next.setOnClickListener(this);

        // set the default values for the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	Display display = getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	
    	int width = size.x;
    	//int height = size.y;
    	screenWidthThird=width/3;
    	
    	countDownTimer = new MyCountDownTimer(timeInSec, 1000);

    	initialize();
    	    	
	}
	
	public void initialize(){
		
		playerScore.setText("Score = 0");
    	score = 0;
    	conScore = 0;
		shootFlag=false;
    	noOfTries=1;
    	text.setVisibility(View.GONE);
    	next.setText("Try Next One");
    	next.setVisibility(View.GONE);
    	timerHasStarted = false;
    	createRandom();		
	}
           
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    
    	switch (item.getItemId()) {
        	case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }    
   
    @Override
    public void onResume() {
        super.onResume();
        
        typeOfGame = Integer.parseInt(prefs.getString("pref_type", "0"));
        levelOfGame = Integer.parseInt(prefs.getString("pref_level", "0"));
        switch (levelOfGame){
		case LEVEL_EASY:
			timeInSec = 9000;
			break;
		case LEVEL_MODERATE:
			timeInSec = 6000;
			break;
		case LEVEL_HARD:
			timeInSec = 3000;
			break;
        }
    }
    
	public void createRandom(){
            	            	
		int divedent;
		int wrongAnswer1;
		int wrongAnswer2;
		
		Random r = new Random();
		int noOne=r.nextInt(13);
		int noTwo=r.nextInt(13);

		rightPosition=r.nextInt(3);
		int result = 0;
		int mixedCase = r.nextInt(2);
		
		switch (typeOfGame){
		case TYPE_MULTIPLY:
			equationLine.setText(String.valueOf(noOne) + " X " + String.valueOf(noTwo)+" = ?");
			result = noOne*noTwo;
			break;
		case TYPE_DIVIDE:
			if (noTwo == 0) noTwo=2;
			divedent = noTwo*noOne;
			equationLine.setText(String.valueOf(divedent) + " / " + String.valueOf(noTwo)+" = ?");
			result = divedent/noTwo;			
			break;
		case TYPE_MIXED:
			if (mixedCase == 0) {
				equationLine.setText(String.valueOf(noOne) + " X " + String.valueOf(noTwo)+" = ?");
				result = noOne*noTwo;
			}
			else {
				if (noTwo == 0) noTwo=2;
				divedent = noTwo*noOne;
				equationLine.setText(String.valueOf(divedent) + " / " + String.valueOf(noTwo)+" = ?");
				result = divedent/noTwo;	
			}
			break;
        }

		wrongAnswer1=r.nextInt(145);
		wrongAnswer2=r.nextInt(145);
		while (wrongAnswer1 == wrongAnswer2 || wrongAnswer1 == result || wrongAnswer2 == result)
		{
			wrongAnswer1=r.nextInt(145);
			wrongAnswer2=r.nextInt(145);
		}
		
		switch (rightPosition){
		case 0:
			leftAnswer.setText(String.valueOf(result));
			middleAnswer.setText(String.valueOf(wrongAnswer1));
			rightAnswer.setText(String.valueOf(wrongAnswer2));
			break;
		case 1:
			leftAnswer.setText(String.valueOf(wrongAnswer1));
			middleAnswer.setText(String.valueOf(result));
			rightAnswer.setText(String.valueOf(wrongAnswer2));
			break;
		case 2:
			leftAnswer.setText(String.valueOf(wrongAnswer1));
			middleAnswer.setText(String.valueOf(wrongAnswer2));
			rightAnswer.setText(String.valueOf(result));
			break;
		}
		
		if (!timerHasStarted) {
			countDownTimer.start();
			timerHasStarted = true;
		
		}
	}
            

	public boolean onTouchEvent(MotionEvent touchevent) 
	{
	
		switch (touchevent.getAction())
		{
			
			case MotionEvent.ACTION_DOWN: 
			{
				x1 = touchevent.getX();
				y1 = touchevent.getY();
				//Toast.makeText(this, "X = " + x1, Toast.LENGTH_SHORT).show();
				//Toast.makeText(this, "Y = " + y1, Toast.LENGTH_SHORT).show();
				
				break;
			}
			case MotionEvent.ACTION_UP: 
			{
				x2 = touchevent.getX();
				y2 = touchevent.getY(); 
				
				ImageView image1 = (ImageView)findViewById(R.id.shooting_image);
				float xImage = image1.getX();
				float yImage = image1.getY();

				
				//Toast.makeText(this, "XImg = " + xImage, Toast.LENGTH_SHORT).show();
				//Toast.makeText(this, "YImg = " + yImage, Toast.LENGTH_SHORT).show();
				
				float increase = 300;
				
				if (y1 > y2 && !shootFlag && y1 >= yImage - increase && y1 <= yImage + increase && x1 >= xImage - increase && x1 <= xImage + increase)
				{
					shootFlag=true;
					countDownTimer.cancel();
					timerHasStarted=false;
					//int collisionStop = 40;

					if (x2 > screenWidthThird*2-50){
						// Right shoot
						float rightX = rightAnswer.getX();
						float rightY = rightAnswer.getY();
						
						View animatedView = findViewById(R.id.shooting_image);
						
						TranslateAnimation animation = new TranslateAnimation(0, -xImage+rightX, 0, -yImage+rightY);
						animation.setDuration(1000);
						animatedView.startAnimation(animation);

						//ImageView image = (ImageView)findViewById(R.id.shooting_image);
						//Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rightmove);
						//animation.
						//animation.setDuration(200);
						//image.startAnimation(animation);
								
						//if (image.getY() < collisionStop) image.clearAnimation();
						
						delay(2, 1200);
					}
					else if (x2 < screenWidthThird+50){
						//Left shoot
						float rightX = leftAnswer.getX();
						float rightY = leftAnswer.getY();
						
						View animatedView = findViewById(R.id.shooting_image);
						
						TranslateAnimation animation = new TranslateAnimation(0, -xImage+rightX, 0, -yImage+rightY);
						animation.setDuration(1000);
						animatedView.startAnimation(animation);

						delay(0, 1200);
					}
					else {
						//Center shoot
						float rightX = middleAnswer.getX();
						float rightY = middleAnswer.getY();
						
						View animatedView = findViewById(R.id.shooting_image);
						
						TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -yImage+rightY);
						animation.setDuration(1000);
						animatedView.startAnimation(animation);
						
						delay(1, 1200);
					}		
				}
				break;
			}
		}
		return true;

	}
	
	public void delay(final int x, final int y) {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
		    //Do something after y ms
			  if (x != 3){
				  if (x == rightPosition) correctAnswer();
				  else inCorrectAnswer();
			  }
		  }
		}, y);		
	}

	public void correctAnswer(){
		score++;
		conScore++;
		playerScore.setText("Score = "+score);
		if (noOfTries == totalTries) results();
		else {
			switch (conScore) {
				case 3:
					text.setText("Good Job");
					break;
				case 5:
					text.setText("Very Good");
					break;
				case 7:
					text.setText("Fantastic");
					break;
				default:
					text.setText("Correct");					
			}

			text.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);			
		}

	}
	
	public void inCorrectAnswer(){
		conScore=0;
		if (noOfTries == totalTries)	results();
		else {
			text.setText("Incorrect");
			text.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
		}
	}
	
	public void results() {
		text.setText("Game Over ! Your score is " + score);
		text.setVisibility(View.VISIBLE);
		next.setText("Play again");
		next.setVisibility(View.VISIBLE);		
	}
	
	
	@Override
    public void onClick(View v) {
        
    	int listenCode = v.getId();
    	
    	if (R.id.next == listenCode)
    	{
    		if (next.getText() == "Play again") initialize();
    		else {
    			next.setVisibility(View.GONE);
    			text.setVisibility(View.GONE);
    			noOfTries++;
    			timerHasStarted=false;
    			shootFlag=false;
    			createRandom();
    		}
        }
    }
	
	
	public class MyCountDownTimer extends CountDownTimer {
	 	public MyCountDownTimer(long startTime, long interval) 
	 	{
	 		super(startTime, interval);
		}

		@Override
		public void onFinish() {
			if (timerHasStarted){
				if (noOfTries == totalTries) results();
				else {
					conScore =0;
					text.setText("Time's up!");
					text.setVisibility(View.VISIBLE);
					shootFlag=true;
					next.setVisibility(View.VISIBLE);
				}			
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//text.setText("" + millisUntilFinished / 1000);
			//remainingTime=(int) (millisUntilFinished / 1000);
		}
	 }
}