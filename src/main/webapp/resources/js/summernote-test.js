(function (factory) {
  /* global define */
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as an anonymous module.
    define(['jquery'], factory);
  } else if (typeof module === 'object' && module.exports) {
    // Node/CommonJS
    module.exports = factory(require('jquery'));
  } else {
    // Browser globals
    factory(window.jQuery);
  }
}(function ($) {

	/**
	* @class plugin.emoji 
	* 
	* Initialize in the toolbar like so:
	*	toolbar: ['insert', ['emojiList']]
	*
	* Emoji Plugin  
	*/
  $.extend($.summernote.plugins, {
    /**
     * @param {Object} context - context object has status of editor.
     */
    'testList': function (context) {
      var self = this;

      // ui has renders to build ui elements.
      //  - you can create a button with `ui.button`
      var ui = $.summernote.ui;
	  
	  // add emoji button
	  context.memo('button.testList', function () {
		// generate all the emojis
		// var list = "";
		// for (i = 0; i < emojis.length; i++) {
			// list += '<img src="' + emojis[i] + '" />';
		// }
		
        var $testList = ui.buttonGroup([
          ui.button({
            className: 'dropdown-toggle',
            contents: '<span style="font-weight: bold">${v}&nbsp;</span> <span class="caret"></span>',
            tooltip: "Insertar Variable",
            data: {
              toggle: 'dropdown'
            }
          }),
          ui.dropdown({
            className: 'dropdown-style',
            //items: emojis, // list of style tag
			items: emojis,
			// contents: "<ol><li>hello</li><li>hello europe</li><li>hello europe</li></ol>",
			callback: function ($dropdown) {
                $dropdown.find('li a').each(function () {
					$(this).click(function() {
						context.invoke("editor.insertText",  '${'+$(this).html()+'}');
					});
                });
			}
          })
        ]).render();
		return $testList;
      });

      // This events will be attached when editor is initialized.
      this.events = {
        // This will be called after modules are initialized.
        'summernote.init': function (we, e) {
			
		  console.log('summernote initialized zzzz', we, e);
        },
        // This will be called when user releases a key on editable.
        'summernote.keyup': function (we, e) {
		
          console.log('summernote keyup zzzz', we, e);
        }
      };

      // This methods will be called when editor is destroyed by $('..').summernote('destroy');
      // You should remove elements on `initialize`.
		this.destroy = function () {
			console.log('summernote destroy zzz');
		};
		
		function addimg(value) {
			var img = $('<img src="' + value + '" style="vertical-align:text-bottom;" data-prevent-popover="" />');
			context.invoke("editor.insertNode", img[0]);
		}
    }
  });
  
}));
