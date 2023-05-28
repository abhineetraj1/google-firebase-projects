// Initialize Firebase
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  appId: "YOUR_APP_ID"
};

firebase.initializeApp(firebaseConfig);

// Create input and button elements
const input = document.createElement("input");
input.type = "file";
input.id = "imageInput";
input.accept = "image/*";
input.style.display = "none";
input.addEventListener("change", handleFileSelect);

const dropArea = document.createElement("div");
dropArea.textContent = "Drag and drop or click to select an image";
dropArea.classList.add("drop-area");
dropArea.addEventListener("dragover", handleDragOver);
dropArea.addEventListener("drop", handleDrop);
dropArea.addEventListener("click", handleClick);

// Append the elements to the body
document.body.appendChild(dropArea);
document.body.appendChild(input);

function handleDragOver(event) {
  event.preventDefault();
}

function handleDrop(event) {
  event.preventDefault();
  const file = event.dataTransfer.files[0];
  processFile(file);
}

function handleClick() {
  input.click();
}

function handleFileSelect() {
  const file = input.files[0];
  processFile(file);
}

function processFile(file) {
  const fileId = Date.now().toString();
  const storageRef = firebase.storage().ref().child(fileId);

  storageRef.put(file)
    .then(() => {
      storageRef.getDownloadURL()
        .then((imageUrl) => {
          performFaceRecognition(imageUrl);
        })
        .catch((error) => {
          console.log("Error getting download URL:", error);
        });
    })
    .catch((error) => {
      console.log("Error uploading image:", error);
    });
}

function performFaceRecognition(imageUrl) {
  const image = new Image();
  image.src = imageUrl;

  image.onload = () => {
    const visionImage = firebase.ml.vision.imageFromImage(image);

    firebase.ml.vision().faceDetector().processImage(visionImage)
      .then((faces) => {
        if (faces.length > 0) {
          console.log("Face detected!");
        } else {
          console.log("No face detected.");
        }
      })
      .catch((error) => {
        console.log("Error performing face recognition:", error);
      });
  };
}
