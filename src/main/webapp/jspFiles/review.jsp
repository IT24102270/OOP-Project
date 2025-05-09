<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Review Management</title>
  <style>
    * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
    }

    body {
        font-family: Arial, sans-serif;
        background-color: #1a252f;
        color: #e0e0e0;
        padding: 20px;
    }

    .navbar {
        display: flex;
        align-items: center;
        background-color: #0d1b26;
        padding: 10px 20px;
        border-radius: 8px;
        margin-bottom: 20px;
    }

    .navbar .logo {
        font-size: 24px;
        font-weight: bold;
        color: #1e90ff;
        margin-right: 30px;
    }

    .navbar a {
        color: #e0e0e0;
        text-decoration: none;
        margin-right: 20px;
        font-size: 14px;
    }

    .navbar a:hover {
        color: #1e90ff;
    }

    .container {
        background-color: #0d1b26;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
    }

    h1 {
        font-size: 24px;
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .add-btn, .apply-btn {
        background-color: #1e90ff;
        color: #fff;
        border: none;
        padding: 8px 16px;
        border-radius: 4px;
        cursor: pointer;
        font-size: 14px;
    }

    .add-btn:hover, .apply-btn:hover {
        background-color: #1c86ee;
    }

    .filters {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
        align-items: center;
    }

    .filters select, .filters input {
        padding: 6px;
        border: 1px solid #3a4d61;
        background-color: #1a252f;
        color: #e0e0e0;
        border-radius: 4px;
    }

    .form-group {
        display: flex;
        flex-direction: column;
        gap: 5px;
        margin-bottom: 15px;
    }

    .form-group label {
        font-size: 14px;
        color: #e0e0e0;
    }

    .form-group input, .form-group textarea {
        padding: 8px;
        border: 1px solid #3a4d61;
        background-color: #1a252f;
        color: #e0e0e0;
        border-radius: 4px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        background-color: #1a252f;
    }

    th, td {
        padding: 12px;
        text-align: left;
        border-bottom: 1px solid #3a4d61;
    }

    th {
        background-color: #0d1b26;
        font-weight: bold;
    }

    td {
        color: #e0e0e0;
    }

    .status {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 12px;
        font-size: 12px;
    }

    .booked {
        background-color: #ffca28;
        color: #1a252f;
    }

    .delete-btn {
        background-color: #ff4444;
        color: #fff;
        border: none;
        padding: 6px 12px;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
    }

    .delete-btn:hover {
        background-color: #cc0000;
    }

    .error-message {
        color: #ff4444;
        margin-bottom: 15px;
    }
  </style>
</head>
<body>
  <div class="navbar">
    <div class="logo">ReviewPro</div>
    <a href="#">Dashboard</a>
    <a href="#">Reviews</a>
    <a href="#">Users</a>
    <a href="#">Maintenance</a>
    <a href="#">Feedback</a>
    <a href="#">Logout</a>
  </div>

  <div class="container">
    <h1>Manage Reviews <button class="add-btn" onclick="showForm()">Add New Review</button></h1>
    <div class="error-message" id="errorMessage" style="display: none;">Error submitting review. Please try again.</div>
    <div class="filters" id="filters">
      <span>Sort By</span>
      <select id="sortBy">
        <option value="reviewID">Review ID</option>
      </select>
      <span>Filter By</span>
      <select id="filterBy">
        <option value="all">All</option>
      </select>
      <button class="apply-btn" onclick="applyFilters()">Apply</button>
    </div>

    <form id="reviewForm" style="display: none;" onsubmit="submitReview(event); return false;">
      <div class="form-group">
        <label for="reviewID">Review ID</label>
        <input type="text" id="reviewID" name="reviewID" required>
      </div>
      <div class="form-group">
        <label for="userID">User ID</label>
        <input type="text" id="userID" name="userID" required>
      </div>
      <div class="form-group">
        <label for="comment">Comment</label>
        <textarea id="comment" name="comment" required></textarea>
      </div>
      <div class="form-group">
        <label for="rating">Rating (1-5)</label>
        <input type="number" id="rating" name="rating" min="1" max="5" required>
      </div>
      <button type="submit" class="add-btn">Submit Review</button>
      <button type="button" class="add-btn" onclick="hideForm()">Cancel</button>
    </form>

    <table id="reviewsTable">
      <tr>
        <th>Review ID</th>
        <th>User ID</th>
        <th>Comment</th>
        <th>Rating</th>
        <th>Actions</th>
      </tr>
      <tr>
        <td>R001</td>
        <td>U001</td>
        <td>Great service!</td>
        <td class="status booked">4</td>
        <td><button class="delete-btn" onclick="deleteReview('R001')">Delete</button></td>
      </tr>
      <tr>
        <td>R002</td>
        <td>U002</td>
        <td>Excellent experience</td>
        <td class="status booked">5</td>
        <td><button class="delete-btn" onclick="deleteReview('R002')">Delete</button></td>
      </tr>
    </table>
  </div>

  <script>
    let reviews = [
      { id: 'R001', userId: 'U001', comment: 'Great service!', rating: 4 },
      { id: 'R002', userId: 'U002', comment: 'Excellent experience', rating: 5 },
      { id: 'R003', userId: 'U003', comment: 'Good', rating: 3 }
    ];

    function showForm() {
      document.getElementById('reviewForm').style.display = 'block';
      document.getElementById('filters').style.display = 'none';
      document.getElementById('reviewsTable').style.display = 'none';
      document.getElementById('errorMessage').style.display = 'none';
    }

    function hideForm() {
      document.getElementById('reviewForm').style.display = 'none';
      document.getElementById('filters').style.display = 'flex';
      document.getElementById('reviewsTable').style.display = 'table';
    }

    function submitReview(event) {
      event.preventDefault(); // Prevent default form submission
      console.log('Submitting review...'); // Debug log

      const reviewID = document.getElementById('reviewID').value.trim();
      const userID = document.getElementById('userID').value.trim();
      const comment = document.getElementById('comment').value.trim();
      const rating = parseInt(document.getElementById('rating').value);

      console.log({ reviewID, userID, comment, rating }); // Debug values

      if (!reviewID || !userID || !comment || isNaN(rating) || rating < 1 || rating > 5) {
        document.getElementById('errorMessage').style.display = 'block';
        return;
      }

      // Check for duplicate review ID
      if (reviews.some(r => r.id === reviewID)) {
        document.getElementById('errorMessage').textContent = 'Review ID already exists. Please use a unique ID.';
        document.getElementById('errorMessage').style.display = 'block';
        return;
      }

      reviews.push({ id: reviewID, userId: userID, comment: comment, rating: rating });
      updateTable();
      hideForm();
      document.getElementById('reviewForm').reset();
    }

    function deleteReview(reviewID) {
      reviews = reviews.filter(r => r.id !== reviewID);
      updateTable();
    }

    function updateTable() {
      const table = document.getElementById('reviewsTable');
      const tbody = table.getElementsByTagName('tbody')[0] || table;
      tbody.innerHTML = '<tr><th>Review ID</th><th>User ID</th><th>Comment</th><th>Rating</th><th>Actions</th></tr>';

      if (reviews.length === 0) {
        tbody.innerHTML += '<tr><td colspan="5">No reviews available.</td></tr>';
      } else {
        reviews.forEach(review => {
          const row = tbody.insertRow();
          row.insertCell().textContent = review.id;
          row.insertCell().textContent = review.userId;
          row.insertCell().textContent = review.comment;
          const ratingCell = row.insertCell();
          ratingCell.innerHTML = `<span class="status booked">${review.rating}</span>`;
          const actionCell = row.insertCell();
          actionCell.innerHTML = `<button class="delete-btn" onclick="deleteReview('${review.id}')">Delete</button>`;
        });
      }
    }

    function applyFilters() {
      alert('Filter applied! (Implement client-side filtering)');
    }

    window.onload = function() {
      updateTable();
      hideForm();
    };
  </script>
</body>
</html>
