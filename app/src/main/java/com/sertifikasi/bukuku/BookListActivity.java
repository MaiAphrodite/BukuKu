package com.sertifikasi.bukuku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sertifikasi.bukuku.adapter.BookAdapter;
import com.sertifikasi.bukuku.database.DatabaseHelper;
import com.sertifikasi.bukuku.model.Book;
import java.util.List;

/**
 * Main activity for displaying the list of books
 * Handles book management operations through a context menu triggered by click
 */
public class BookListActivity extends AppCompatActivity implements BookAdapter.OnBookActionsListener {
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private FloatingActionButton fabAdd;
    private LinearLayout emptyStateLayout;
    private DatabaseHelper dbHelper;
    private List<Book> books;    /**
     * Initialize UI components and set up the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        
        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadBooks();
    }

    /**
     * Refresh book list when returning to this activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadBooks(); // Refresh data ketika kembali ke activity ini
    }

    /**
     * Initialize all view components
     */
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        dbHelper = new DatabaseHelper(this);
        
        // Set action bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Daftar Buku");
        }
    }

    /**
     * Configure the RecyclerView with layout manager
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Set up click listeners for UI components
     */
    private void setupClickListeners() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookListActivity.this, AddEditBookActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Load books from database and update the UI
     */
    private void loadBooks() {
        books = dbHelper.getAllBooks();
        
        if (books.isEmpty()) {
            // Show empty state
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            // Show book list
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
            
            if (adapter == null) {
                adapter = new BookAdapter(this, books);
                adapter.setOnBookActionsListener(this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateBooks(books);
            }
        }
    }    /**
     * Handle click on book item - show context menu with options
     * @param book The book that was clicked
     * @param view The view that was clicked (for positioning context menu)
     */
    @Override
    public void onBookClick(Book book, View view) {
        showBookContextMenu(book, view);
    }

    /**
     * Show context menu with book action options
     * @param book The selected book
     * @param anchorView The view to anchor the popup menu to
     */
    private void showBookContextMenu(Book book, View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);
        
        // Add menu items
        popup.getMenu().add(0, 1, 0, "View Details");
        popup.getMenu().add(0, 2, 0, "Edit");
        popup.getMenu().add(0, 3, 0, "Delete");
        
        // Handle menu item clicks
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return handleBookAction(item.getItemId(), book);
            }
        });
        
        popup.show();
    }

    /**
     * Handle the selected book action from context menu
     * @param actionId The ID of the selected action
     * @param book The book to perform the action on
     * @return true if the action was handled
     */
    private boolean handleBookAction(int actionId, Book book) {
        switch (actionId) {
            case 1: // View Details
                openBookDetails(book);
                return true;
            case 2: // Edit
                openEditBook(book);
                return true;
            case 3: // Delete
                showDeleteConfirmation(book);
                return true;
            default:
                return false;
        }
    }

    /**
     * Open book details view
     * @param book The book to view details for
     */
    private void openBookDetails(Book book) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("book_id", book.getId());
        startActivity(intent);
    }

    /**
     * Open edit book activity
     * @param book The book to edit
     */
    private void openEditBook(Book book) {
        Intent intent = new Intent(this, AddEditBookActivity.class);
        intent.putExtra("book_id", book.getId());
        startActivity(intent);
    }

    /**
     * Show confirmation dialog before deleting a book
     * @param book The book to delete
     */
    private void showDeleteConfirmation(Book book) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus buku \"" + book.getTitle() + "\"?")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBook(book);
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    /**
     * Delete a book from the database
     * @param book The book to delete
     */
    private void deleteBook(Book book) {
        dbHelper.deleteBook(book.getId());
        Toast.makeText(this, "Buku \"" + book.getTitle() + "\" berhasil dihapus", Toast.LENGTH_SHORT).show();
        loadBooks(); // Refresh the list after deletion
    }    /**
     * Create options menu for logout and info
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    /**
     * Handle options menu item selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        } else if (item.getItemId() == R.id.action_info) {
            showInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Logout user and return to login screen
     */
    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Show app information screen
     */
    private void showInfo() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }
}
