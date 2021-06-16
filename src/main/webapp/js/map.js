var rootLocation;
var availableRegionPattern = '';
var availableRegion = '';

var idRoute = 'route';
mapboxgl.accessToken = 'pk.eyJ1IjoiZXZyb205OCIsImEiOiJja3BteWc2eTMwNnlrMnVudXFsZjhhdW44In0.IQYTsD8Zi1M2RVxqyB8-Fw';
var map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [35.049669, 48.466164],
    zoom: 11
});

/**
 * translate longitude and latitude in text address
 * @param url
 * @returns {Map<any, any>}
 */
map.on('click', function (e) {
    let fromRegionId = document.getElementById('fromRegionId');
    let toRegionId = document.getElementById('toRegionId');
    let from = document.getElementById('from');
    let to = document.getElementById('to');
    let fromName = document.getElementById('fromName');
    let toName = document.getElementById('toName');
    let separator = ', ';
    let url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + e.lngLat.lng + ',' + e.lngLat.lat + '.json';

    $.ajax({
        url: url,
        type: 'get',
        async: true,
        data: {
            types: 'place,region,poi',
            access_token: mapboxgl.accessToken,
            language: 'ru'
        },
        success: function (resp) {
            let features = new Map();
            resp.features.forEach(elem => features.set(elem.place_type[0], elem));

            if (features.get('poi') && features.get('poi').context) {
                features.get('poi').context.forEach(function (elem) {
                    let id = elem.id.split('.')[0];
                    if (id === 'region') {
                        features.set(id, elem);
                    }
                });
            }

            let region = '';
            if (!features.get('region') || !(region = availableRegionPattern.exec(features.get('region').text))) {
                alert("Region not available");
                return;
            }

            if (from.value === '' || (from.value !== '' && to.value !== '')) {
                fromRegionId.value = extractRegionId(region[0])
                fromName.value = extractLocationName(features);
                from.value = e.lngLat.lng + separator + e.lngLat.lat;
                toName.value = '';
                to.value = '';
                return;
            }
            toRegionId.value = extractRegionId(region[0]);
            toName.value = extractLocationName(features);
            to.value = e.lngLat.lng + separator + e.lngLat.lat;
        },
        error: function () {
            alert("MapBox connection refused")
        }
    });
});

function extractLocationName(features) {
    let name = features.get('poi') && features.get('poi').properties &&
    features.get('poi').properties.address ?
        features.get('poi').properties.address + ', ' :
        '';

    name += features.get('place') && features.get('place').place_name ?
        features.get('place').place_name :
        features.get('region') && features.get('region').place_name ?
            features.get('region').place_name :
            '';
    return name;
}

function extractRegionId(region) {
    for (let i = 0; i < availableRegion.length; i++) {
        if (availableRegion[i].name === region || availableRegion[i].region === region) {
            return availableRegion[i].id;
        }
    }
}

$(document).ready(function () {
    rootLocation = $('#homeLocation').attr('href');
    $.get(rootLocation + 'availableRegion', {}, function (resp) {
        resp = JSON.parse(resp);
        resp.availableRegions.forEach(
            city => availableRegionPattern += city.region + '|' + city.name + '|'
        )
        availableRegionPattern = availableRegionPattern.slice(0, availableRegionPattern.length - 1);
        availableRegionPattern = new RegExp(availableRegionPattern);
        availableRegion = resp.availableRegions;
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