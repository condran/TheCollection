/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 3/11/12
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */

// This sets up default behaviours across all the pages
$(function() {

    // Enable date controls
    $('.datepicker').datepicker();

    // Enable type-ahead controls
    $('.ajax-typeahead').typeahead({
        source: function(query, process) {
            return $.ajax({
                url: $(this)[0].$element[0].dataset.link,
                type: 'get',
                data: {query: query},
                dataType: 'json',
                success: function(data) {
                    return typeof data == 'undefined' ? false : process(data);
                }
            });
        }
    });
});
