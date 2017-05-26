String.prototype.format = function () {
    var str = this;
    for (var i = 0; i < arguments.length; i++) {
        var reg = new RegExp("\\{" + i + "\\}", "gm");
        str = str.replace(reg, arguments[i]);
    }
    return str;
}

function doClick(buttonId, e) {

    var key;

    if (window.event)
        key = window.event.keyCode; // IE
    else
        key = e.which; // firefox

    if (key == 13) {
        // Get the button the user wants to have clicked
        var btn = $("[id$='" + buttonId + "']"); // document.getElementById(buttonName);
        if (btn != null) { // If we find the button click it
            btn.click();
            if ($.browser.msie)
                e.keyCode = 0;
            else
                e.preventDefault();
        }
    }
}

function openWin(url) {
    newwindow = window.open(url, "ReportWindows", "status=no,toolbar=no,location=no,menubar=no,resizable,width=1008,height=690,scrollbars,left=100,top=50","_blank");
    if (window.focus) {
        newwindow.focus()
    }
    if (!newwindow.closed) {
        newwindow.focus()
    }

    return false;
}

$(document).ready(function () {
    $.fn.modal.prototype.constructor.Constructor.DEFAULTS.backdrop = 'static';

    /*$('.table-hover').on('click', 'tbody tr', function(event) {
        $(this).addClass('active').siblings().removeClass('active');
    });*/

    $('.modal.draggable>.modal-dialog').draggable({
        cursor: 'move',
        handle: '.modal-header'
    });
    $(document).on('focus', ':input', function () {
        $(this).attr('autocomplete', 'off');
    });
    $(".modalForm").on('shown.bs.modal', function () {
        $(this).find('input:text')[0].focus();
    });
    $('.modalConfirmDelete').on('shown.bs.modal', function (e) {
        var index = $(e.relatedTarget).data('index');
        var idObject = $(e.relatedTarget).data('entity-id');
        var message = $(e.relatedTarget).data('message');
        message = message.format('<b>' + idObject + '</b>');
        $(e.currentTarget).find('p:first').html(message);
        $(e.currentTarget).find('input[name="index"]').val(index);
    });
    $(document).on("shown.bs.dropdown", ".dropdown", function () {
        // calculate the required sizes, spaces
        var $ul = $(this).children(".dropdown-menu");
        var $button = $(this).children(".dropdown-toggle");
        var ulOffset = $ul.offset();
        // how much space would be left on the top if the dropdown opened that direction
        var spaceUp = (ulOffset.top - $button.height() - $ul.height()) - $(window).scrollTop();
        // how much space is left at the bottom
        var spaceDown = $(window).scrollTop() + $(window).height() - (ulOffset.top + $ul.height());
        // switch to dropup only if there is no space at the bottom AND there is space at the top, or there isn't either but it would be still better fit
        if (spaceDown < 0 && (spaceUp >= 0 || spaceUp > spaceDown))
            $(this).addClass("dropup");
    }).on("hidden.bs.dropdown", ".dropdown", function () {
        // always reset after close
        $(this).removeClass("dropup");
    });
});


function handleSubmitHide(xhr, status, args, dialogVar) {
    if (!args.validationFailed) {
        $('#' + dialogVar).modal('hide');
    }
}

function handleSubmitShow(xhr, status, args, dialogVar) {
    if (args.validationFailed === undefined) {
        $('#' + dialogVar).modal('show');
    }
}

