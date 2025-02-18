import React, { useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const WebSocketChat = () => {
    const [stompClient, setStompClient] = useState(null);
    const [username, setUsername] = useState("");
    const [accessToken, setAccessToken] = useState("");
    const [isConnected, setIsConnected] = useState(false);
    const [broadcastMessage, setBroadcastMessage] = useState("");
    const [privateMessage, setPrivateMessage] = useState("");
    const [recipient, setRecipient] = useState("");
    const [broadcastMessages, setBroadcastMessages] = useState([]);
    const [privateMessages, setPrivateMessages] = useState([]);

    const connectWebSocket = () => {
        if (!username.trim()) {
            alert("Please enter a username!");
            return;
        }
        if (!accessToken.trim()) {
            alert("Please enter an access token!");
            return;
        }

        const socket = new SockJS("http://localhost:8888/chat");
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            connectHeaders: { Authorization: `Bearer ${accessToken}` }, // ✅ Send token on connect
            onConnect: () => {
                console.log(`Connected as ${username}`);

                // Subscribe to public messages
                client.subscribe("/topic/public", (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    setBroadcastMessages((prev) => [...prev, receivedMessage]);
                });

                // Subscribe to private messages (specific user queue)
                client.subscribe(`/user/${username}/queue/private`, (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    console.log("Private message:", receivedMessage);
                    setPrivateMessages((prev) => [...prev, receivedMessage]);
                });

                setIsConnected(true);
            },
            onStompError: (frame) => {
                console.error("WebSocket error: ", frame.headers["message"]);
            },
            onDisconnect: () => {
                console.log("Disconnected from WebSocket");
                setIsConnected(false);
            },
        });

        client.activate();
        setStompClient(client);
    };

    // ✅ Ensure token is included in EVERY message
    const sendBroadcast = () => {
        if (stompClient && broadcastMessage.trim() !== "") {
            stompClient.publish({
                destination: "/app/broadcast",
                headers: { Authorization: `Bearer ${accessToken}` }, // ✅ Send token
                body: JSON.stringify({ from: username, text: broadcastMessage }),
            });
            setBroadcastMessage("");
        }
    };

    const sendPrivateMessage = () => {
        if (stompClient && privateMessage.trim() !== "" && recipient.trim() !== "") {
            stompClient.publish({
                destination: "/app/private",
                headers: { Authorization: `Bearer ${accessToken}` }, // ✅ Send token
                body: JSON.stringify({ from: username, text: privateMessage, to: recipient }),
            });
            setPrivateMessage("");
        }
    };

    return (
        <div style={{ maxWidth: "600px", margin: "auto", padding: "20px", fontFamily: "Arial, sans-serif" }}>
            {/* ✅ Access Token Input (Always Visible) */}
            <div style={{ marginBottom: "10px" }}>
                <label>Access Token:</label>
                <input
                    type="text"
                    placeholder="Enter access token"
                    value={accessToken}
                    onChange={(e) => setAccessToken(e.target.value)}
                    style={{ width: "100%", padding: "5px", marginBottom: "10px" }}
                />
            </div>

            {!isConnected ? (
                <div style={{ textAlign: "center" }}>
                    <h2>Enter Username</h2>
                    <input
                        type="text"
                        placeholder="Your username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        style={{ width: "80%", padding: "5px", marginBottom: "10px" }}
                    />
                    <br />
                    <button onClick={connectWebSocket} style={{ padding: "10px 20px", cursor: "pointer" }}>Join Chat</button>
                </div>
            ) : (
                <div>
                    <h2>Welcome, {username}!</h2>

                    {/* Broadcast Messaging */}
                    <div style={{ marginBottom: "20px" }}>
                        <h3>Broadcast Chat</h3>
                        <input
                            type="text"
                            placeholder="Enter broadcast message"
                            value={broadcastMessage}
                            onChange={(e) => setBroadcastMessage(e.target.value)}
                            style={{ width: "80%", padding: "5px" }}
                        />
                        <button onClick={sendBroadcast} style={{ marginLeft: "10px", padding: "5px 10px" }}>Send</button>
                        <ul>
                            {broadcastMessages.map((msg, index) => (
                                <li key={index}><strong>{msg.from}:</strong> {msg.text}</li>
                            ))}
                        </ul>
                    </div>

                    {/* Private Messaging */}
                    <div>
                        <h3>Private Chat</h3>
                        <input
                            type="text"
                            placeholder="Recipient username"
                            value={recipient}
                            onChange={(e) => setRecipient(e.target.value)}
                            style={{ width: "40%", padding: "5px", marginRight: "10px" }}
                        />
                        <input
                            type="text"
                            placeholder="Enter private message"
                            value={privateMessage}
                            onChange={(e) => setPrivateMessage(e.target.value)}
                            style={{ width: "40%", padding: "5px" }}
                        />
                        <button onClick={sendPrivateMessage} style={{ marginLeft: "10px", padding: "5px 10px" }}>Send</button>
                        <ul>
                            {privateMessages.map((msg, index) => (
                                <li key={index}><strong>{msg.from} to {msg.to}:</strong> {msg.text}</li>
                            ))}
                        </ul>
                    </div>
                </div>
            )}
        </div>
    );
};

export default WebSocketChat;
