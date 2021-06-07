var idRoute = 'route';
mapboxgl.accessToken = '';
var map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [35.049669, 48.466164],
    zoom: 12
});

map.on('click', function (e) {
    let from = document.getElementById('from');
    let separator = ', ';
    if (from.value === '') {
        from.value = e.lngLat.lng + separator + e.lngLat.lat;
        return;
    }
    let to = document.getElementById('to');
    if (to.value === '') {
        to.value = e.lngLat.lng + separator + e.lngLat.lat;
        return;
    }
    from.value = e.lngLat.lng + separator + e.lngLat.lat;
    to.value = '';
});

function printRoute(geoJson) {
    if (map.getSource(idRoute)) {
        map.removeLayer(idRoute);
        map.removeSource(idRoute);
    }
    map.addSource(idRoute, {
        'type': 'geojson',
        'data': {
            'type': 'Feature',
            'properties': {},
            'geometry': {
                'type': 'LineString',
                'coordinates': geoJson
            }
        }
    });
    map.addLayer({
        'id': idRoute,
        'type': 'line',
        'source': idRoute,
        'layout': {
            'line-join': 'round',
            'line-cap': 'round'
        },
        'paint': {
            'line-color': 'rgb(255,0,0)',
            'line-width': 8
        }
    });
}