
Counter = new ReactiveVar(0);

if (Meteor.isClient) {
  Template.counter.helpers({
    counter: function () {
      return Counter.get();
    },
  });

  Template.counter.events({
    'click .increment': function () {
      
      Meteor.call('increment', function(err, res) {
        Counter.set(res);
      });
    }
  });

}


if (Meteor.isServer) {
  Meteor.startup(function () {
    Meteor.methods({
      "increment": function() {
        Counter.set(Counter.get() + 1);
        return Counter.get()
      }
    });
  });
}

