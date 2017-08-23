All time periods (gametime) are in seconds.
Distance is always in meters

### Create Game (player to server)
#### Request
```json
{ "type": "GameCreate",
  "players": 4,
  "bounds": [
    {"lat": 2.5, "lng": 52.5},
    {"lat": 2.9, "lng": 52.5},
    {"lat": 2.9, "lng": 52.9},
    {"lat": 2.5, "lng": 52.9}
  ],
  "gametime": 300}
```
#### Response
```json
{ "type": "GameCreated",
  "gamecode": "Ab$2D!" }
```


### Join Game (player to server)
#### Request
```json
{ "type": "GameJoin",
  "gamecode": "Ab$2D!",
  "username": "Sijmen" }
```
#### Response
```json
{ "type": "GameJoined",
  "bounds": [
    {"lat": 2.5, "lng": 52.5},
    {"lat": 2.9, "lng": 52.5},
    {"lat": 2.9, "lng": 52.9},
    {"lat": 2.5, "lng": 52.9}
  ],
 "gametime": 300,
 "waitingfor": ["Bram", "Pijke", "Sijmen"],
 "ready": ["David"]}
```


### Ready to Play  (player to server)
#### Request
```json
{ "type": "ReadyToPlay"}
```

### Ready to Play Update  (server to player)
#### Request
```json
{ "type": "ReadyToPlayUpdatePacket",
  "watingfor": ["Bram", "Pijke"],
  "ready": ["David", "Sijmen"]}
```


### Game Start!  (server to player)
#### Request
```json
{ "type": "GameStart", 
  "players":[
    { "username": "Sijmen", "color": "RED" },
    { "username": "David", "color": "BLUE" }
  ]
  }
```


### Location update  (player to server)
#### Request
```json
{ "type": "LocationUpdate", 
  "location": {"lat": 2.5, "lng": 52.5 } }
```


### New Area  (server to player)
#### Request
```json
{ "type": "NewArea",
  "points": [
    {"lat": 2.9, "lng": 52.5},
    {"lat": 2.9, "lng": 52.9},
    {"lat": 2.5, "lng": 52.9}
  ],
  "username": "Sijmen" }
```


### New LinePoint  (server to player)
#### Request
```json
{ "type": "AddLinePoints",
  "id": 8,
  "location": {
    "lat": 12.4,
    "lng": 52.6
  },
  "username": "Sijmen"} 
```

### Remove LinePoint  (server to player)
#### Request
```json
{ "type": "RemoveLinePoints",
  "ids": [8, 16, 17, 19, 23]} 
```


### Game End  (server to player)
#### Request
```json
{ "type": "GameEnd" }
```


### Game Stats  (server to player)
#### Request
```json
{ "type": "GameStats", 
  "timeleft": 120,
  "players": [
    {"username": "Sijmen", "fieldPercentage": 23.5, "distanceTraveled": 1200},
    {"username": "David", "fieldPercentage": 35.8, "distanceTraveled": 3800}
  ]}
```
