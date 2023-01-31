## Drones
### Build and Run
To build application run command:
```
mvn clean install
```
To run database go to `main/resources/docker` folder and run command:
```
docker-compose up -d
```
To run application go to root folder and run command:
```
 mvn spring-boot:run
```

### Usage examples
1) registerDrone - POST /api/v0.1/drones

Request:
```
{
    "serialNumber": "dronchik_45",
    "model": "Lightweight",
    "weightLimit": 100,
    "batteryCapacity": 20
}
```
Response:
```
{
    "id": "2d89f286-4735-427e-8370-5daedfde0a4e",
    "serialNumber": "dronchik_45",
    "model": "Lightweight",
    "weightLimit": 100,
    "batteryCapacity": 20,
    "state": "IDLE"
}
```
2) loadDrone - POST /api/v0.1/drones{id}/load

Request:
```
[
    {
        "name": "SHOPIN",
        "weight": 50,
        "code": "ASD_57"
    }
]
```
Response:
```
{
    "id": "378f0280-5905-47c7-8723-e5ef772446b3",
    "serialNumber": "dronchik_2",
    "model": "Lightweight",
    "weightLimit": 100,
    "batteryCapacity": 15,
    "state": "LOADING",
    "medications": [
        {
            "id": "075546f6-1db1-4b0e-b436-ee5df75b3b19",
            "name": "SHOPIN",
            "weight": 50,
            "code": "ASD_57",
            "image": null
        }
    ]
}
```
3) getMedications - GET /api/v0.1/drones/{id}/medications

Response:
```
[
    {
        "name": "Polizec",
        "weight": 50,
        "code": "drc_1",
        "image": null
    },
    {
        "name": "Bolk",
        "weight": 20,
        "code": "abc_132",
        "image": null
    }
]
```
4) getBatteryLevel - GET /api/v0.1/drones/{id}/battery

Response:
```
{
    "id": "7c13c0f1-4442-4d03-b58d-ba63eb741544",
    "batteryCapacity": 100
}
```
5) getAvailableDrones - GET /api/v0.1/drones/available

Response:
```
[
    {
        "id": "e80faaf3-1a67-47f3-9007-be729c292962",
        "serialNumber": "ABC_12334",
        "model": "Middleweight",
        "weightLimit": 200,
        "batteryCapacity": 100,
        "state": "IDLE"
    },
    {
        "id": "16834ae2-0b74-4054-a94f-9eba9472ffc3",
        "serialNumber": "BGF_14345",
        "model": "Cruiserweight",
        "weightLimit": 350,
        "batteryCapacity": 75,
        "state": "IDLE"
    }
]
```