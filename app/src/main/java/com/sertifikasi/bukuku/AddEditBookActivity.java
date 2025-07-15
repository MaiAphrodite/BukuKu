package com.sertifikasi.bukuku;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.sertifikasi.bukuku.database.DatabaseHelper;
import com.sertifikasi.bukuku.model.Book;

public class AddEditBookActivity extends AppCompatActivity {
    private EditText etTitle, etAuthor, etYear, etGenre;
    private Spinner spinnerStatus;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private Book currentBook;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);
        
        initViews();
        checkEditMode();
        setupClickListeners();
    }    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etYear = findViewById(R.id.etYear);
        etGenre = findViewById(R.id.etGenre);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new DatabaseHelper(this);
        
        // Setup spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }

    private void checkEditMode() {
        int bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId != -1) {
            isEditMode = true;
            currentBook = dbHelper.getBook(bookId);
            if (currentBook != null) {
                fillFormWithBookData();
                setTitle("Edit Buku");
                btnSave.setText("Update");
            }
        } else {
            setTitle("Tambah Buku");
            btnSave.setText("Simpan");
        }
    }    private void fillFormWithBookData() {
        etTitle.setText(currentBook.getTitle());
        etAuthor.setText(currentBook.getAuthor());
        etYear.setText(String.valueOf(currentBook.getYearPublished()));
        etGenre.setText(currentBook.getGenre());
        
        String status = currentBook.getStatus();
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerStatus.getAdapter();
        int position = adapter.getPosition(status);
        if (position >= 0) {
            spinnerStatus.setSelection(position);
        }
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });
    }    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        if (!validateInputs(title, author, yearStr, genre, status)) {
            return;
        }

        int year = Integer.parseInt(yearStr);

        if (isEditMode) {
            currentBook.setTitle(title);
            currentBook.setAuthor(author);
            currentBook.setYearPublished(year);
            currentBook.setGenre(genre);
            currentBook.setStatus(status);
            
            int result = dbHelper.updateBook(currentBook);
            if (result > 0) {
                Toast.makeText(this, "Buku berhasil diupdate", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal mengupdate buku", Toast.LENGTH_SHORT).show();
            }
        } else {
            Book newBook = new Book(title, author, year, genre, status);
            long result = dbHelper.addBook(newBook);
            if (result != -1) {
                Toast.makeText(this, "Buku berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menambahkan buku", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs(String title, String author, String yearStr, String genre, String status) {
        if (title.isEmpty()) {
            etTitle.setError("Judul tidak boleh kosong");
            etTitle.requestFocus();
            return false;
        }

        if (author.isEmpty()) {
            etAuthor.setError("Penulis tidak boleh kosong");
            etAuthor.requestFocus();
            return false;
        }

        if (yearStr.isEmpty()) {
            etYear.setError("Tahun terbit tidak boleh kosong");
            etYear.requestFocus();
            return false;
        }

        try {
            int year = Integer.parseInt(yearStr);
            if (year < 1000 || year > 2100) {
                etYear.setError("Tahun tidak valid");
                etYear.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etYear.setError("Tahun harus berupa angka");
            etYear.requestFocus();
            return false;
        }        if (genre.isEmpty()) {
            etGenre.setError("Genre tidak boleh kosong");
            etGenre.requestFocus();
            return false;
        }

        return true;
    }
}
