// Initialize Firebase
var firebaseConfig = {
  apiKey: "your-api-key",
  authDomain: "your-auth-domain",
  projectId: "your-project-id",
  storageBucket: "your-storage-bucket",
  messagingSenderId: "your-messaging-sender-id",
  appId: "your-app-id"
};

firebase.initializeApp(firebaseConfig);

// Get a reference to the Firebase database
var database = firebase.database();

// Set up the quiz questions
var questions = [
  {
    question: "What is the capital of France?",
    choices: ["London", "Paris", "Madrid", "Berlin"],
    correctAnswer: "Paris"
  },
  {
    question: "What is the largest country in the world?",
    choices: ["Russia", "China", "United States", "Canada"],
    correctAnswer: "Russia"
  },
  {
    question: "What is the currency of Japan?",
    choices: ["Dollar", "Pound", "Yen", "Euro"],
    correctAnswer: "Yen"
  }
];

// Set up the quiz state
var currentQuestion = 0;
var score = 0;

// Display the current question
function displayQuestion() {
  var question = questions[currentQuestion];
  var questionText = document.createTextNode(question.question);
  var choices = question.choices;

  var questionElement = document.createElement("div");
  questionElement.appendChild(questionText);

  for (var i = 0; i < choices.length; i++) {
    var choice = choices[i];
    var choiceText = document.createTextNode(choice);

    var choiceElement = document.createElement("div");
    choiceElement.appendChild(choiceText);
    choiceElement.setAttribute("data-choice", choice);
    choiceElement.addEventListener("click", handleChoiceClick);

    questionElement.appendChild(choiceElement);
  }

  var quizElement = document.getElementById("quiz");
  quizElement.innerHTML = "";
  quizElement.appendChild(questionElement);
}

// Handle a choice click
function handleChoiceClick(event) {
  var choice = event.target.getAttribute("data-choice");
  var question = questions[currentQuestion];

  if (choice === question.correctAnswer) {
    score++;
  }

  currentQuestion++;

  if (currentQuestion < questions.length) {
    displayQuestion();
  } else {
    displayScore();
  }
}

// Display the final score
function displayScore() {
  var scoreText = document.createTextNode("Your score is " + score + "/" + questions.length);

  var scoreElement = document.createElement("div");
  scoreElement.appendChild(scoreText);

  var quizElement = document.getElementById("quiz");
  quizElement.innerHTML = "";
  quizElement.appendChild(scoreElement);

  // Save the score to the Firebase database
  var user = firebase.auth().currentUser;
  database.ref("scores/" + user.uid).set({
    score: score,
    timestamp: Date.now()
  });
}

// Display the quiz when the page loads
window.onload = function() {
  displayQuestion();
};
