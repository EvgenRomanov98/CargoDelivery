mapboxgl.accessToken = 'pk.eyJ1IjoiZXZyb205OCIsImEiOiJja3BqbWJ2dGUweWNxMnZvOG56bTg3OWcyIn0.dzdxhoOvGG6pBQz0uiDFmQ';
var map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [35.221505, 47.265413],
    zoom: 15
});

// map.on('load', function () {
//     map.addSource('route', {
//         'type': 'geojson',
//         'data': {
//             'type': 'Feature',
//             'properties': {},
//             'geometry': {
//                 'type': 'LineString',
//                 'coordinates': [[], []]
//             }
//         }
//     });
//     map.addLayer({
//         'id': 'route',
//         'type': 'line',
//         'source': 'route',
//         'layout': {
//             'line-join': 'round',
//             'line-cap': 'round'
//         },
//         'paint': {
//             'line-color': 'rgba(94,255,140,0.5)',
//             'line-width': 8
//         }
//     });
// });

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
    map.addSource('route', {
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
        'id': 'route',
        'type': 'line',
        'source': 'route',
        'layout': {
            'line-join': 'round',
            'line-cap': 'round'
        },
        'paint': {
            'line-color': 'rgba(94,255,140,0.5)',
            'line-width': 8
        }
    });
}