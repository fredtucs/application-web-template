function execCommandButtonAjax(data, onBegin, onComplete, onSuccess, validationFailed) {
    var status = data.status;
    switch (status) {
        case 'begin': {
            onBegin();
            break;
        }
        case 'complete': {
            onComplete();
            break;
        }
        case 'success': {
            console.log('Validation Failed ' + validationFailed);
            if(!validationFailed){
                onSuccess();
            }
            break;
        }
    }
}