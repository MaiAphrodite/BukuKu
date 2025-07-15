package com.sertifikasi.bukuku;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.sertifikasi.bukuku.database.DatabaseHelper;
import com.sertifikasi.bukuku.model.Book;

/**
 * Activity to display detailed information about a book
 * Shows book details in a read-only view format
 */
public class BookDetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvAuthor, tvYear, tvGenre, tvStatus;
    private DatabaseHelper dbHelper;
    private Book currentBook;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_book_detail);
        
        initViews();
        loadBookData();
    }    /**
     * Initialize UI components and set up database helper
     */
    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvYear = findViewById(R.id.tvYear);
        tvGenre = findViewById(R.id.tvGenre);
        tvStatus = findViewById(R.id.tvStatus);
        dbHelper = new DatabaseHelper(this);
        
        setTitle("Detail Buku");
    }

    private void loadBookData() {
        bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId != -1) {
            currentBook = dbHelper.getBook(bookId);
            if (currentBook != null) {
                displayBookInfo();
            } else {
                Toast.makeText(this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID buku tidak valid", Toast.LENGTH_SHORT).show();
            finish();
        }
    }    /**
     * Display book information in the UI components
     */
    private void displayBookInfo() {
        tvTitle.setText(currentBook.getTitle());
        tvAuthor.setText(currentBook.getAuthor());
        tvYear.setText(String.valueOf(currentBook.getYearPublished()));
        tvGenre.setText(currentBook.getGenre());
        tvStatus.setText(currentBook.getStatus());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data jika ada perubahan dari edit
        if (currentBook != null) {
            Book updatedBook = dbHelper.getBook(bookId);
            if (updatedBook != null) {
                currentBook = updatedBook;
                displayBookInfo();
            }
        }
    }
}
