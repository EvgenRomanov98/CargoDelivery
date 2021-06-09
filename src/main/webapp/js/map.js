var rootLocation;
var availableRegion = [];

var idRoute = 'route';
mapboxgl.accessToken = 'pk.eyJ1IjoiZXZyb205OCIsImEiOiJja3BteWc2eTMwNnlrMnVudXFsZjhhdW44In0.IQYTsD8Zi1M2RVxqyB8-Fw';
var map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [35.049669, 48.466164],
    zoom: 12
});

/**
 * translate longitude and latitude in text address
 * @param url
 * @returns {Map<any, any>}
 */
function reverseGeocoding(url) {
    let features = new Map();
    $.get(url,
        {
            types: 'place,region',
            access_token: mapboxgl.accessToken,
            language: 'ru'
        },
        function (resp) {
            if (resp.features.length >= 0) {
                features.set(resp.features[0].place_type[0], resp.features[0]);
            }
            if (resp.features.length > 1) {
                features.set(resp.features[1].place_type[0], resp.features[1]);
            }

            if (availableRegion.indexOf(features.get('region').text) === -1) {
                alert("Region not available");
                return undefined;
            }
            return features;
        });
}

map.on('click', function (e) {
    let from = document.getElementById('from');
    let to = document.getElementById('to');
    let separator = ', ';
    let url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + e.lngLat.lng + ',' + e.lngLat.lat + '.json';

    let features = reverseGeocoding(url);
    if (features === undefined) {
        return;
    }

    if (from.value === '' || (from.value !== '' && to.value !== '')) {
        from.value = features.get('place') !== '' && features.get('place') !== undefined ?
            resp.features[0].place_name :
            e.lngLat.lng + separator + e.lngLat.lat;
        to.value = '';
        return;
    }
    to.value = features.get('place') !== '' && features.get('place') !== undefined ?
        resp.features[0].place_name :
        e.lngLat.lng + separator + e.lngLat.lat;
});

$(document).ready(function () {
    rootLocation = $('#homeLocation').attr('href');
    $.get(rootLocation + 'availableRegion', {}, function (resp) {
        resp = JSON.parse(resp);
        resp.availableRegions.forEach(
            region => availableRegion.push(region.key)
        )
        console.log(availableRegion);
    });
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