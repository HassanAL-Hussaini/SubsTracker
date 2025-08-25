# Hassan's Contributions – SubsTracker Project

This document highlights the **major contributions and endpoints** I developed for the **SubsTracker** team project, where I played a **leading role** in AI services, payment integration, database design, and overall project structure. I also led my team during the **Capstone phase**, managing production environment access and ensuring smooth delivery.

---

## 💡 Core Idea 
- SubsTracker helps users manage all their subscriptions—whether **digital** (like Netflix, Shahid, Spotify) or **service-based** (like gym memberships, car washes).  
- The platform not only tracks and organizes subscriptions but also integrates **AI-powered financial analysis** and **payment automation**.  

---

## ⚙️ Key Features

### 🔐 Subscription Management
- Add, update, and delete subscriptions.  
- Keep track of active vs. expired subscriptions.  

### 🤖 AI-Driven Insights
- Analyze user spending habits.  
- Suggest cheaper or more competitive alternatives.  
- Provide personalized financial recommendations.  

### 💳 Payment Automation
- Securely process payments via **Moyasar Gateway**.  
- Save and manage payment cards.  
- Check transaction status directly through the app.  

### 👥 User Management
- Differentiate between subscribed and unsubscribed users.  
- Allow admins to view all active customers.  

---

## 🚀 My Core Contributions
- **Designed and implemented key AI-powered and Payment-related endpoints.**
- **Structured the program architecture** to maintain clean, modular, and scalable code.
- **Designed and normalized the database schema** for efficient data handling.
- **Led the development team** during the capstone project, ensuring high-quality coding practices and integration.
- **Managed the production environment**, granting full team access and overseeing deployment readiness.

---

## 📌 Endpoints I Developed

### Subscription Management
2. **Add Subscription**
- **Endpoint:** `POST /api/subscriptions/add`
- Adds a new subscription for a user.
- **Response:** Created subscription details.

3. **Update Subscription**
- **Endpoint:** `PUT /api/subscriptions/update/{subscriptionId}`
- Updates subscription information such as price, category, or billing period.
- **Response:** Updated subscription details.

4. **Delete Subscription**
- **Endpoint:** `DELETE /api/subscriptions/delete/{subscriptionId}`
- Removes a subscription by ID.
- **Response:** Success message with status 200.

---

### AI-Driven Services
12. **Generate AI Subscription Alternative**
- **Endpoint:** `GET /api/ai/alternative/{subscriptionId}`
- Generates and returns an **AI-powered alternative recommendation** for a given subscription, including potential savings and competitor prices.
- **Response:** AI-generated alternative subscription with recommendations.

13. **Get AI Spending Analysis by User ID**
- **Endpoint:** `GET /api/ai/analysis/{userId}`
- Retrieves personalized **AI analysis** of user spending patterns, with financial recommendations for cost optimization.
- **Response:** Insights and recommendations tailored to user profile.

---

### User Management
19. **Get All Subscribed Users**
- **Endpoint:** `GET /api/users/subscribed`
- Returns a list of all users with active subscriptions.

20. **Get All Unsubscribed Users**
- **Endpoint:** `GET /api/users/unsubscribed`
- Returns a list of all users without active subscriptions.

---

### Payment Integration
21. **Process Payment**
- **Endpoint:** `POST /api/payment/process/{userId}`
- Initiates a payment request for the given user via **Moyasar Payment Gateway**.
- **Response:** Transaction status (initiated, captured, failed).

22. **Check Payment Status**
- **Endpoint:** `GET /api/payment/status/{userId}`
- Verifies payment transaction status for a given user.
- **Response:** JSON response with payment state.

23. **Get Payment Card by User ID**
- **Endpoint:** `GET /api/payment/card/{userId}`
- Retrieves stored payment card details associated with the user.

24. **Add Payment Card for User**
- **Endpoint:** `POST /api/payment/card/add/{userId}`
- Associates a new payment card with the user’s account.

---

## 🎯 Impact of My Work
- Enabled **secure and seamless payment processing** using Moyasar integration.
- Provided **AI-powered financial insights** for smarter user decisions.
- Built a **scalable backend structure** ensuring maintainability.
- Delivered **database models and schemas** that power subscription, AI, and payment modules.
- **Led the team** effectively, ensuring smooth collaboration and deployment in production.

---

## 🗄️ Database Design

Below is the **ERD diagram** that represents the database structure I designed and implemented for the SubsTracker project:

![Database Design](Untitled%20diagram%20_%20Mermaid%20Chart-2025-08-23-010749.png)

---

## PowerPoint

[Download the PowerPoint](<https://github.com/HassanAL-Hussaini/SubsTracker/blob/master/CapstoneThree(SubsTracker).pptx?raw=1>)  
[View on GitHub](<https://github.com/HassanAL-Hussaini/SubsTracker/blob/master/CapstoneThree(SubsTracker).pptx>)

---

## 👤 Author
**Hassan Al Hossaini**  
Full Stack Developer | Backend Focus (Spring Boot, React, PostgreSQL)  
Lead Developer – SubsTracker Project

---

✨ With my contributions, the **SubsTracker** project evolved into a powerful platform combining subscription management, AI financial insights, and payment automation — all within a clean and production-ready architecture.
