import pandas as pd
N = 20000

txt_files_df = pd.read_csv('spotify_millsongdata.csv')

txt_files_df = txt_files_df.drop(columns=['link'])

txt_files_df = txt_files_df.iloc[:N]
txt_files_df['Id'] = txt_files_df.reset_index().index
print(txt_files_df.head())

for idx, row in txt_files_df.iterrows():
    f = open("file_"+str(row.Id)+".txt",'w', encoding="utf-8")
    
    buffer = str(row.Id) + '\n' + str(row.artist) + '\n' + str(row.song) + '\n' + str(row.text)
        
    f.write((buffer))
    f.close()
