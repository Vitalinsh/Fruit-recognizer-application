# Fruit_recognizer_application

Dataset - "Fruits 360 dataset" from kaggle: https://www.kaggle.com/moltean/fruits


There are 2 ways to run server and classificator:

1. Without using Docker:
	1. Install python 3.6 interpreter
	2. Install all packages from requirements.txt
		To do this use command in command line:
			pip install -r requirements.txt
	
	3. Run server.py file to start the server
	4. Run send_picture_for_checking.py script to check the work of classificator. It send picture to server and print classification result.
	
2. Using Docker:
	1. From /root directory run command to build Docker image: 
    	sudo docker build -t recognitron
	2. From /root directory run command to run Docker image in a container:
    	sudo docker run -d -p 4000:80 recognitron
		

To create Android Debug APK:

1. Using Docker (NOTE: needs quite a lot of time and stable internet connection to build APK, although nothing beside the Docker is needed to build):
    1. Set permition to execute for client/gradlew script (sudo chmod +x gradlew).
    1. From /client directory run command to build Docker image: 
    	sudo docker build . -t fruit-recognizer
    2. From /client directory run command to run Docker image in a container:
    	sudo docker run --mount type=bind,source="$(pwd)/..",target=/app fruit-recognizer
	NOTE: Yes, you need to bind mount the whole project, with server, because code in client depends on .git directory - this will be fixed as soon as client will reside in its own repo.
    3. Generated APK is located under client/app/build/outputs/apk/debug
    
2. Using Gradle (NOTE: JDK8 and Android SDK 28 with Android Build Tools 28.0.3 are needed in order to build APK):
    1. Set permition to execute for client/gradlew script (sudo chmod +x .gradlew).
    2. From /client directory run command to build APK: 
    	sudo ./gradlew clean build
	on Linux, or
	gradlew clean build
	on Windows
    3. Generated APK is located under client/app/build/outputs/apk/debug

