let score = JSON.parse(localStorage.getItem('score')) || {
	wins :0,
	losses :0,
	ties : 0
};

updateScoreElement();


//console.log(localStorage.getItem('message'));

//console.log(localStorage.getItem('score'));
console.log(JSON.parse(localStorage.getItem('score')));

function playGame(playerMove){

	pickComputerMove();
	console.log(computerMove);

	let result = '';

	if(playerMove === 'paper'){
		if(computerMove === 'paper'){
			result = 'tie';
		}
		else if(computerMove === 'scissors'){
			result = 'You lose';
		}
		else if(computerMove === 'rock'){
			result = 'You Win';
		}
	}

	else if(playerMove === 'rock'){
		if(computerMove === 'rock'){
			result = 'tie';
		}
		else if(computerMove === 'paper'){
			result = 'You lose';
		}
		else if(computerMove === 'scissors'){
			result = 'You Win';
		}

	}

	else if(playerMove === 'scissors'){
		
		if(computerMove === 'scissors'){
			result = 'tie';
		}
		else if(computerMove === 'rock'){
			result = 'You lose';
		}
		else if(computerMove === 'paper'){
			result = 'You Win';
		}
	}

	if(result === 'You Win'){
		score.wins += 1;
		//score.win = score.win+1;
	}

	else if(result === 'You lose'){
		score.losses += 1;
	}

	else if(result === 'tie'){
		score.ties += 1;
	}

	//localStorage.setItem('message','hello');

	localStorage.setItem('score', JSON.stringify(score));

	updateScoreElement();

	document.querySelector('.js-result').innerHTML = result;

	document.querySelector('.js-moves').innerHTML = `You
		<img src="images/${playerMove}-emoji.png" class="move-icon" alt="">
		<img src="images/${computerMove}-emoji.png" class="move-icon" alt="">
		Computer`;

	//alert(`You picked ${playerMove} and \nComputer picked ${computerMove} \nResult is ${result} \nWins : ${score.wins} \nLosses : ${score.losses} \nTies : ${score.ties}`);
}


function updateScoreElement(){
	document.querySelector('.js-score').innerHTML = `Wins : ${score.wins} \nLosses : ${score.losses} \nTies : ${score.ties}`;
}


let computerMove = '';		//scope ????


function pickComputerMove(){
const randomNumber = Math.random();

if(randomNumber>= 0 && randomNumber<1/3){
	/*console.log('rock');*/
	computerMove = 'rock';
}
else if(randomNumber>=1/3 && randomNumber<2/3){
	/*console.log('paper');*/
	computerMove = 'paper';
}
else if(randomNumber>=2/3 && randomNumber<=1){
	/*console.log('scissors');*/
	computerMove = 'scissors';
}

return 5; //once returned it stops the function and does not read wt is after return
}