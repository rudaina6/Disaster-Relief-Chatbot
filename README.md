# 🆘 Disaster Relief Chatbot

A Java-based chatbot that provides emergency disaster information such as shelter locations, emergency contacts, and disaster statistics. It integrates with a MySQL database to simulate the retrieval of data from a continually updating source. It also uses the OpenAI API to intelligently process queries.

## 🌟 Features

- 🔍 Query emergency information for floods, droughts, epidemics, and earthquakes
- 🗂 Access shelter locations and emergency contacts by region
- 🧠 Uses OpenAI's GPT API for smart query processing
- 💬 Conversational interface via JavaFX
- 🛠 Built with OOP design principles (modular and extensible)

## 🖼️ UI Preview

> ![image](https://github.com/user-attachments/assets/85b3e3a5-589d-49a0-9b78-58804967c609)
>
> ![image](https://github.com/user-attachments/assets/641ed8e4-606b-4494-b2ac-74a6afbd4f96)
> 
> ![image](https://github.com/user-attachments/assets/db1b4398-3514-4923-9460-220d65c4d2dd)
> 
> ![image](https://github.com/user-attachments/assets/0203fb7d-cbbc-4c18-9fed-df7aea725ccd)

## ⚙️ Technologies Used

- **Java (OOP)**
- **JavaFX** – for GUI
- **MySQL** – for storing emergency data used by the chatbot
- **JDBC** – for DB connection
- **OpenAI API** – for NLP capabilities
- **dotenv-java** – to load environment variables securely

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/rudaina6/DisasterReliefChatbot.git
cd DisasterReliefChatbot
```

### 2. Set Up Environment Variables

Create and update the .env file in the root directory. 

### 3. Set Up the Database

Use the provided SQL script in chatbot_db.sql and run it in MySQL Workbench or CLI to create the tables and data.

### 4. Run the App

- Open the project in IntelliJ or VS Code
- Run Chatbot.java
- The chatbot window will open — start chatting!

## 📚 Future Improvements

🌐 Add location-based search using GPS/IP

📊 Add dashboard UI for statistical summaries

🗣️ Enable multilingual support
