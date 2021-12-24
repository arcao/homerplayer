package com.studio4plus.homerplayer.ui.classic;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.studio4plus.homerplayer.R;
import com.studio4plus.homerplayer.ui.MainActivity;
import com.studio4plus.homerplayer.ui.UiControllerMain;

public class FragmentFileSelect extends Fragment {
    public UiControllerMain controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_select, container, false);

        controller = ((MainActivity) requireActivity()).controller;

        String[] files = controller.getBookFileNames();
        int selectedFile = controller.getPlayedFileIndex();

        Adapter adapter = new Adapter(files, selectedFile, this::onSelectItem);
        RecyclerView fileList = view.findViewById(R.id.fileList);
        Button returnButton = view.findViewById(R.id.returnButton);

        fileList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        fileList.setAdapter(adapter);
        fileList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        fileList.scrollToPosition(Math.max(0, selectedFile - 1));

        returnButton.setOnClickListener(v -> hide());

        return view;
    }

    private void onSelectItem(int value) {
        controller.selectFile(value);
        hide();
    }

    public void show(FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.mainContainer, this);
        transaction.commitNow();
    }

    public void hide() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(this);
        transaction.commitNow();
    }

    public interface ClickListener {
        void onClick(int position);
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        public final String[] items;
        private final int selectedItem;
        private final ClickListener onClick;

        public Adapter(String[] items, int selectedItem, ClickListener onClick) {
            this.items = items;
            this.selectedItem = selectedItem;
            this.onClick = onClick;
        }

        public static int getColorFromThemeAttribute(@NonNull Context context, @AttrRes int attr) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{attr});
            int color = ta.getColor(0, 0);
            ta.recycle();
            return color;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_select_file_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TextView view = (TextView) holder.itemView;
            view.setText(items[position]);
            view.setOnClickListener(v -> onClick.onClick(position));
            if (selectedItem == position) {
                view.setTextColor(getColorFromThemeAttribute(view.getContext(), R.attr.buttonVolumeBackground));
            } else {
                view.setTextColor(getColorFromThemeAttribute(view.getContext(), R.attr.playbackTextColor));
            }

        }

        @Override
        public int getItemCount() {
            return items.length;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
