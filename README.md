# System Information Collector

## Overview

The **System Information Collector** is a powerful Kotlin-based application designed to gather and provide comprehensive details about the system it runs on. It exposes this information through various API endpoints, making it easy to monitor and manage system resources.

## 🚀 Features

- **Operating System Information:**  
  Get details about the OS, including its name, version, architecture, manufacturer, build number, and install date.

- **Computer System Details:**  
  Access information such as the manufacturer, model, and serial number.

- **Processor Details:**  
  Gather insights about the processor, including its name, physical and logical core counts, maximum frequency, and current frequency.

- **Memory Information:**  
  View memory statistics, including total, available, and used memory, as well as cache size and swap memory details.

- **Battery Information:**  
  Retrieve battery-related data such as remaining capacity, time remaining, voltage, temperature, cycle count, charging status, and overall battery health.

- **Display Information:**  
  Get details about connected displays, including device names, resolutions, and refresh rates.

- **Software Information:**  
  Fetch data on running processes, system uptime, JVM information, and environment variables.

- **Storage Information:**  
  Monitor total, used, and free storage space.

- **User Environment Information:**  
  View details like the username, home directory, language, country, timezone, and locale.

- **Detailed Storage Usage:**  
  Analyze and categorize file storage by type and size.

- **System Commands:**  
  Execute commands to:
  - 🔌 Shutdown
  - 🔄 Restart
  - 💤 Sleep
  - 🗄️ Hibernate
  - 🚪 Log off
  - 🔒 Lock the system

---

## 📡 API Endpoints

- **`/operatingSystemInfo`** - Retrieves operating system information.
- **`/systemInfo`** - Fetches computer system details.
- **`/processorDetails`** - Provides processor details.
- **`/memory`** - Displays memory statistics.
- **`/JvmDetails`** - Returns JVM information.
- **`/detailedStorageUsage`** - Analyzes and categorizes storage usage.
- **`/shutdown`** - Shuts down the system.
- **`/restart`** - Restarts the system.
- **`/sleep`** - Puts the system to sleep.
- **`/hibernate`** - Hibernates the system.
- **`/logoff`** - Logs off the current user.
- **`/lock`** - Locks the system.

---

## 🛠️ Usage

### Step 1: Set up the project
Ensure you have the required environment to build and run the application.

### Step 2: Start the application
Run the application to expose the endpoints.

### Step 3: Access system information
You can use tools like `curl`, Postman, or any HTTP client to interact with the API.

#### If you have downloaded the jar file, you can run the application using the following command:

```bash
java -jar system-information-collector-1.0.0.jar
```


### Example Request via a browser:
To retrieve operating system information:

```bash
http://localhost:8080/operatingSystemInfo
