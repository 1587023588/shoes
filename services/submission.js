// Compatibility bridge: re-export folder index using CommonJS to avoid
// runtime module resolution issues in some devtool environments.
try {
  module.exports = require('./submission/index.js');
} catch (e) {
  // Defensive: export no-op stubs so requiring this file never throws
  module.exports = {
    saveSubmission: async function (p) { return p; },
    fetchSubmissionsByUser: async function () { return []; },
    fetchSubmissionsBySpuId: async function () { return []; },
    uploadAndSaveSubmission: async function (p) { return p; },
  };
}
