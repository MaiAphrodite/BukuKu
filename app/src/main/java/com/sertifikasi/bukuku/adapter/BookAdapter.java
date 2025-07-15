package com.sertifikasi.bukuku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sertifikasi.bukuku.R;
import com.sertifikasi.bukuku.model.Book;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;
    private Context context;
    private OnBookActionsListener listener;    public interface OnBookActionsListener {
        void onBookClick(Book book, View view);
    }
    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public void setOnBookActionsListener(OnBookActionsListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }   
    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvYear, tvGenre, tvStatus;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews();
            setupListeners();
        }

        private void initializeViews() {
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvGenre = itemView.findViewById(R.id.tvGenre);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }        private void setupListeners() {
            // Click to show context menu with options
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onBookClick(books.get(position), v);
                    }
                }
            });
        }

        public void bind(Book book) {
            tvTitle.setText(book.getTitle());
            tvAuthor.setText("Penulis: " + book.getAuthor());
            tvYear.setText("Tahun: " + book.getYearPublished());
            tvGenre.setText("Genre: " + book.getGenre());
            tvStatus.setText("Status: " + book.getStatus());
        }
    }
}
