type SharedTrip = {
    trajet: {
        id: number,
        depart: string,
        departLatitude: number,
        departLongitude: number,
        arrivee: string,
        arriveeLatitude: number,
        arriveeLongitude: number,
        moyenTransport: string,
        kco2: number,
    }
    userId: number,
    email: string,
    username: string
}

export default SharedTrip;